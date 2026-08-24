import { Mesa } from './mesa.model';
import { Usuario } from './usuario.model';
import { ItemPedido } from './item-pedido.model';

export type StatusComanda = 'ABERTA' | 'PAGA' | 'CANCELADA';

export interface Comanda {
  id: number;
  mesa: Mesa;
  usuario: Usuario;
  statusComanda: StatusComanda;
  dataAbertura: string;
  total: number;
  itens: ItemPedido[];
}
