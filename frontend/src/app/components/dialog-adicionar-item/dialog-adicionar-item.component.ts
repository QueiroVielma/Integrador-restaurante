import { CurrencyPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Produto } from '../../models/produto.model';

export interface DialogAdicionarItemData {
  produtos: Produto[];
}

export interface DialogAdicionarItemResult {
  produtoId: number;
  quantidade: number;
}

@Component({
  selector: 'app-dialog-adicionar-item',
  standalone: true,
  imports: [
    CurrencyPipe,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './dialog-adicionar-item.component.html'
})
export class DialogAdicionarItemComponent {
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<DialogAdicionarItemComponent, DialogAdicionarItemResult>);
  readonly data = inject<DialogAdicionarItemData>(MAT_DIALOG_DATA);

  readonly form = this.fb.nonNullable.group({
    produtoId: [null as number | null, Validators.required],
    quantidade: [1, [Validators.required, Validators.min(1)]]
  });

  confirmar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { produtoId, quantidade } = this.form.getRawValue();
    this.dialogRef.close({ produtoId: produtoId!, quantidade });
  }

  cancelar(): void {
    this.dialogRef.close();
  }
}
