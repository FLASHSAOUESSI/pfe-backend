import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContactMessage } from '../models/contact-message';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private apiUrl = `${environment.apiUrl}/api/contact`;

  constructor(private http: HttpClient) { }

  // Envoyer un nouveau message
  sendMessage(message: ContactMessage): Observable<ContactMessage> {
    return this.http.post<ContactMessage>(this.apiUrl, message);
  }

  // Obtenir tous les messages (admin seulement)
  getAllMessages(): Observable<ContactMessage[]> {
    return this.http.get<ContactMessage[]>(this.apiUrl);
  }

  // Obtenir les messages non répondus (admin seulement)
  getUnrespondedMessages(): Observable<ContactMessage[]> {
    return this.http.get<ContactMessage[]>(`${this.apiUrl}/unresponded`);
  }

  // Répondre à un message (admin seulement)
  respondToMessage(messageId: number, response: string): Observable<ContactMessage> {
    return this.http.post<ContactMessage>(`${this.apiUrl}/${messageId}/respond`, response);
  }
} 