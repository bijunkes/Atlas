import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
    LucideAngularModule,
    Wallet,
    Github,
    Linkedin,
    Instagram
} from 'lucide-angular';

@Component({
    selector: 'app-footer',
    standalone: true,
    imports: [
        RouterLink,
        LucideAngularModule
    ],
    templateUrl: './footer.html'
    })
    export class FooterComponent {

    readonly Wallet = Wallet;
    readonly Github = Github;
    readonly Linkedin = Linkedin;
    readonly Instagram = Instagram;

}