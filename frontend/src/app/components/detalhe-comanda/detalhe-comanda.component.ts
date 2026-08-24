import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { forkJoin } from 'rxjs';
import { Comanda } from '../../models/comanda.model';
import { Produto } from '../../models/produto.model';
import { ComandaService } from '../../services/comanda.service';
import { ProdutoService } from '../../services/produto.service';
import {
  DialogAdicionarItemComponent,
  DialogAdicionarItemResult
} from '../dialog-adicionar-item/dialog-adicionar-item.component';

@Component({
  selector: 'app-detalhe-comanda',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './detalhe-comanda.component.html',
  styleUrl: './detalhe-comanda.component.css'
})
export class DetalheComandaComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);
  private readonly comandaService = inject(ComandaService);
  private readonly produtoService = inject(ProdutoService);

  comanda: Comanda | null = null;
  produtos: Produto[] = [];
  carregando = true;
  colunas = ['produto', 'quantidade', 'preco', 'subtotal'];

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.carregando = true;

    forkJoin({
      comanda: this.comandaService.buscarPorId(id),
      produtos: this.produtoService.listar()
    }).subscribe({
      next: ({ comanda, produtos }) => {
        this.comanda = comanda;
        this.produtos = produtos.filter(p => p.ativo);
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open('Não foi possível carregar a comanda.', 'Fechar', { duration: 4000 });
      }
    });
  }

  adicionarItem(): void {
    if (!this.comanda) {
      return;
    }

    this.dialog
      .open(DialogAdicionarItemComponent, {
        width: '420px',
        data: { produtos: this.produtos }
      })
      .afterClosed()
      .subscribe((resultado?: DialogAdicionarItemResult) => {
        if (!resultado || !this.comanda) {
          return;
        }

        this.comandaService
          .adicionarItem(this.comanda.id, resultado.produtoId, resultado.quantidade)
          .subscribe({
            next: () => {
              this.snackBar.open('Item adicionado.', 'OK', { duration: 2500 });
              this.carregar();
            },
            error: (err) => {
              const mensagem = err?.error?.mensagem ?? 'Não foi possível adicionar o item.';
              this.snackBar.open(mensagem, 'Fechar', { duration: 4000 });
            }
          });
      });
  }

  fecharComanda(): void {
    if (!this.comanda) {
      return;
    }

    const confirmar = window.confirm('Fechar esta comanda e liberar a mesa?');
    if (!confirmar) {
      return;
    }

    this.comandaService.fecharComanda(this.comanda.id).subscribe({
      next: () => {
        this.snackBar.open('Comanda fechada. Mesa liberada.', 'OK', { duration: 3000 });
        this.carregar();
        this.router.navigate(['/']);
      },
      error: (err) => {
        const mensagem = err?.error?.mensagem ?? 'Não foi possível fechar a comanda.';
        this.snackBar.open(mensagem, 'Fechar', { duration: 4000 });
      }
    });
  }

  subtotal(quantidade: number, preco: number): number {
    return quantidade * preco;
  }
}
