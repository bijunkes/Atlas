    import { Component } from '@angular/core';
    import { LucideAngularModule, Wallet, Menu } from 'lucide-angular';
    import { RouterLink } from '@angular/router';
    import { NgIf } from '@angular/common';

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [
        RouterLink,
        NgIf,
        LucideAngularModule
    ],
    templateUrl: './navbar.html'
})

export class NavbarComponent {

    readonly Wallet = Wallet;
    readonly Menu = Menu;

    menuOpen = false;

    toggleMenu(): void {
        this.menuOpen = !this.menuOpen;
    }

    scrollTo(id: string) {
        this.menuOpen = false;

        const element = document.getElementById(id);

        if (!element) return;

        const lenis = (window as any).lenis;

        if (lenis) {
            lenis.scrollTo(element, {
                offset: -20
            });
        }
    }

}