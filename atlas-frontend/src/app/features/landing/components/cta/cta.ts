import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
    selector: 'app-cta',
    imports: [RouterLink],
    templateUrl: './cta.html',
})
export class CtaComponent {

    private router = inject(Router);

    createAccount(): void {
        this.router.navigate(['/register']);
    }

}