import { NgClass } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Comanda } from '../../models/comanda.model';
import { Mesa } from '../../models/mesa.model';
import { ComandaService } from '../../services/comanda.service';
import { MesaService } from '../../services/mesa.service';

interface MesaPainel {
  mesa: Mesa;
  comanda?: Comanda;
}

@Component({
  selector: 'app-painel-mesas',
  standalone: true,
  imports: [
    NgClass,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './painel-mesas.component.html',
  styleUrl: './painel-mesas.component.css'
})
export class PainelMesasComponent implements OnInit {
  private readonly mesaService = inject(MesaService);
  private readonly comandaService = inject(ComandaService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  mesas: MesaPainel[] = [];
  carregando = true;

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;

    forkJoin({
      mesas: this.mesaService.listar(),
      comandas: this.comandaService.listarComandasAbertas()
    }).subscribe({
      next: ({ mesas, comandas }) => {
        this.mesas = mesas
          .sort((a, b) => a.numero - b.numero)
          .map(mesa => ({
            mesa,
            comanda: comandas.find(c => c.mesa.id === mesa.id)
          }));
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open(
          'Não foi possível carregar as mesas. Confira se a API está em http://localhost:8080.',
          'Fechar',
          { duration: 5000 }
        );
      }
    });
  }

  selecionar(item: MesaPainel): void {
    if (item.mesa.statusMesa === 'OCUPADA' && item.comanda) {
      this.router.navigate(['/comandas', item.comanda.id]);
      return;
    }

    if (item.mesa.statusMesa === 'LIVRE') {
      const confirmar = window.confirm(`Abrir comanda na mesa ${item.mesa.numero}?`);
      if (!confirmar) {
        return;
      }

      this.comandaService.abrirComanda(item.mesa.id, environment.usuarioIdPadrao).subscribe({
        next: (comanda) => {
          this.snackBar.open(`Comanda aberta na mesa ${item.mesa.numero}.`, 'OK', { duration: 2500 });
          this.carregar();
          this.router.navigate(['/comandas', comanda.id]);
        },
        error: (err) => {
          const mensagem = err?.error?.mensagem ?? 'Não foi possível abrir a comanda.';
          this.snackBar.open(mensagem, 'Fechar', { duration: 4000 });
        }
      });
      return;
    }

    this.snackBar.open('Mesa reservada. Libere ou altere o status pelo caixa.', 'OK', { duration: 3000 });
  }
}
