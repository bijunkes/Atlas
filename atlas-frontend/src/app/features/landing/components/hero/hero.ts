import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LucideAngularModule, Check } from 'lucide-angular';

@Component({
    selector: 'app-hero',
    standalone: true,
    imports: [
        RouterLink,
        LucideAngularModule
    ],
    templateUrl: './hero.html'
    })
    export class HeroComponent {

    readonly Check = Check;

}