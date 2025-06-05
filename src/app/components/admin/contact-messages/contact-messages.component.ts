import { Component, OnInit } from '@angular/core';
import { ContactService } from '../../../services/contact.service';
import { ContactMessage } from '../../../models/contact-message';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ResponseDialogComponent } from './response-dialog/response-dialog.component';

@Component({
  selector: 'app-contact-messages',
  templateUrl: './contact-messages.component.html',
  styleUrls: ['./contact-messages.component.scss']
})
export class ContactMessagesComponent implements OnInit {
  messages: ContactMessage[] = [];
  displayedColumns: string[] = ['name', 'email', 'subject', 'timestamp', 'status', 'actions'];
  isLoading = true;

  constructor(
    private contactService: ContactService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadMessages();
  }

  loadMessages(): void {
    this.isLoading = true;
    this.contactService.getAllMessages().subscribe({
      next: (messages) => {
        this.messages = messages;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des messages:', error);
        this.snackBar.open(
          'Une erreur est survenue lors du chargement des messages.',
          'Fermer',
          { duration: 5000 }
        );
        this.isLoading = false;
      }
    });
  }

  openResponseDialog(message: ContactMessage): void {
    const dialogRef = this.dialog.open(ResponseDialogComponent, {
      width: '600px',
      data: message
    });

    dialogRef.afterClosed().subscribe(response => {
      if (response) {
        this.respondToMessage(message.id!, response);
      }
    });
  }

  private respondToMessage(messageId: number, response: string): void {
    this.contactService.respondToMessage(messageId, response).subscribe({
      next: (updatedMessage) => {
        const index = this.messages.findIndex(m => m.id === messageId);
        if (index !== -1) {
          this.messages[index] = updatedMessage;
        }
        this.snackBar.open('Réponse envoyée avec succès', 'Fermer', { duration: 3000 });
      },
      error: (error) => {
        console.error('Erreur lors de l\'envoi de la réponse:', error);
        this.snackBar.open(
          'Une erreur est survenue lors de l\'envoi de la réponse.',
          'Fermer',
          { duration: 5000 }
        );
      }
    });
  }

  viewMessage(message: ContactMessage): void {
    this.dialog.open(ResponseDialogComponent, {
      width: '600px',
      data: { ...message, readonly: true }
    });
  }
} 