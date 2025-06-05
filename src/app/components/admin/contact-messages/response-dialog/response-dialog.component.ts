import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ContactMessage } from '../../../../models/contact-message';

@Component({
  selector: 'app-response-dialog',
  templateUrl: './response-dialog.component.html',
  styleUrls: ['./response-dialog.component.scss']
})
export class ResponseDialogComponent {
  responseForm: FormGroup;
  readonly: boolean;

  constructor(
    private dialogRef: MatDialogRef<ResponseDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ContactMessage & { readonly?: boolean },
    private fb: FormBuilder
  ) {
    this.readonly = data.readonly || false;
    this.responseForm = this.fb.group({
      response: ['', [Validators.required, Validators.minLength(10)]]
    });

    if (this.data.adminResponse) {
      this.responseForm.patchValue({ response: this.data.adminResponse });
    }
    if (this.readonly) {
      this.responseForm.disable();
    }
  }

  onSubmit(): void {
    if (this.responseForm.valid && !this.readonly) {
      this.dialogRef.close(this.responseForm.get('response')?.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 