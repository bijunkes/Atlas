import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
    Target,
    TrendingUp,
    LayoutDashboard,
    ShieldCheck,
    LucideAngularModule
} from 'lucide-angular';

@Component({
    selector: 'app-benefits',
    standalone: true,
    imports: [
        RouterLink,
        LucideAngularModule
    ],
    templateUrl: './benefits.html'
})

export class BenefitsComponent {

    readonly Target = Target;
    readonly TrendingUp = TrendingUp;
    readonly LayoutDashboard = LayoutDashboard;
    readonly ShieldCheck = ShieldCheck;

}