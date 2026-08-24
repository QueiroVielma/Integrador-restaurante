import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Comanda } from '../models/comanda.model';

@Injectable({ providedIn: 'root' })
export class ComandaService {
  private readonly url = `${environment.apiUrl}/comandas`;

  constructor(private readonly http: HttpClient) {}

  listarComandasAbertas(): Observable<Comanda[]> {
    return this.http.get<Comanda[]>(this.url);
  }

  buscarPorId(comandaId: number): Observable<Comanda> {
    return this.http.get<Comanda>(`${this.url}/${comandaId}`);
  }

  abrirComanda(mesaId: number, usuarioId: number): Observable<Comanda> {
    return this.http.post<Comanda>(this.url, { mesaId, usuarioId });
  }

  adicionarItem(comandaId: number, produtoId: number, quantidade: number): Observable<Comanda> {
    return this.http.post<Comanda>(`${this.url}/${comandaId}/itens`, { produtoId, quantidade });
  }

  fecharComanda(comandaId: number): Observable<Comanda> {
    return this.http.put<Comanda>(`${this.url}/${comandaId}/fechar`, {});
  }
}
