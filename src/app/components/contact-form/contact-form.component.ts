import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ContactService } from '../../services/contact.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-contact-form',
  templateUrl: './contact-form.component.html',
  styleUrls: ['./contact-form.component.scss']
})
export class ContactFormComponent implements OnInit {
  contactForm: FormGroup;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private contactService: ContactService,
    private snackBar: MatSnackBar
  ) {
    this.contactForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      subject: ['', [Validators.required, Validators.minLength(3)]],
      message: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.contactForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      
      this.contactService.sendMessage(this.contactForm.value).subscribe({
        next: () => {
          this.snackBar.open(
            'Votre message a été envoyé avec succès. Nous vous répondrons bientôt.',
            'Fermer',
            { duration: 5000 }
          );
          this.contactForm.reset();
        },
        error: (error) => {
          console.error('Erreur lors de l\'envoi du message:', error);
          this.snackBar.open(
            'Une erreur est survenue lors de l\'envoi du message. Veuillez réessayer.',
            'Fermer',
            { duration: 5000 }
          );
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });
    }
  }

  // Getters pour faciliter l'accès aux contrôles du formulaire dans le template
  get nameControl() { return this.contactForm.get('name'); }
  get emailControl() { return this.contactForm.get('email'); }
  get subjectControl() { return this.contactForm.get('subject'); }
  get messageControl() { return this.contactForm.get('message'); }
} 