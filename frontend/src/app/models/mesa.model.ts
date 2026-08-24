export type StatusMesa = 'LIVRE' | 'OCUPADA' | 'RESERVADA';

export interface Mesa {
  id: number;
  numero: number;
  statusMesa: StatusMesa;
}
