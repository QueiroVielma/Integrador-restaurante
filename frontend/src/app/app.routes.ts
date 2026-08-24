import { Routes } from '@angular/router';
import { PainelMesasComponent } from './components/painel-mesas/painel-mesas.component';
import { DetalheComandaComponent } from './components/detalhe-comanda/detalhe-comanda.component';

export const routes: Routes = [
  { path: '', component: PainelMesasComponent },
  { path: 'comandas/:id', component: DetalheComandaComponent },
  { path: '**', redirectTo: '' }
];
