import { Component, inject } from '@angular/core';
import { ToastService } from '../services/toast';

@Component({
    selector: 'app-toast',
    templateUrl: './toast.html'
})
export class ToastComponent {

    toastService = inject(ToastService);

}