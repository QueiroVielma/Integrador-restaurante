import { Produto } from './produto.model';

export interface ItemPedido {
  id: number;
  produto: Produto;
  quantidade: number;
  precoUnitario: number;
}
