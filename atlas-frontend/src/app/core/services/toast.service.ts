import { Injectable, signal } from '@angular/core';

export interface Toast {

    title: string;
    message: string;
    type: 'success' | 'error' | 'warning' | 'info';

}

@Injectable({
    providedIn: 'root'
})
export class ToastService {

    toast = signal<Toast | null>(null);

    show(toast: Toast) {

        this.toast.set(toast);

        setTimeout(() => {

        this.toast.set(null);

        }, 4000);

    }

    close() {
        this.toast.set(null);
    }

}