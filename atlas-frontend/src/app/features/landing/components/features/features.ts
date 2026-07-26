import { Component } from '@angular/core';

@Component({
    selector: 'app-features',
    standalone: true,
    templateUrl: './features.html'
})
export class FeaturesComponent {

    features = [
        {
        title: 'Dashboard financeiro',
        description: 'Visão consolidada do seu patrimônio, receitas e despesas em tempo real.',
        type: 'bars'
        },
        {
        title: 'Organização por categorias',
        description: 'Cada gasto classificado automaticamente para facilitar sua análise.',
        type: 'categories'
        },
        {
        title: 'Controle de gastos',
        description: 'Acompanhe a evolução dos seus gastos e identifique padrões.',
        type: 'line'
        },
        {
        title: 'Metas financeiras',
        description: 'Defina objetivos e acompanhe o progresso com clareza visual.',
        type: 'goals'
        },
        {
        title: 'Acompanhamento de investimentos',
        description: 'Distribuição da carteira e rentabilidade sempre visíveis.',
        type: 'donut'
        },
        {
        title: 'Relatórios inteligentes',
        description: 'Insights automáticos para você tomar decisões mais assertivas.',
        type: 'reports'
        }
    ];

    categories = [
        { name: 'Moradia', active: true },
        { name: 'Transporte', active: false },
        { name: 'Alimentação', active: true },
        { name: 'Lazer', active: false },
        { name: 'Saúde', active: false },
        { name: 'Educação', active: true }
    ];

    reports = [
        {
        label: 'Receita mensal',
        value: 'R$ 18.250',
        color: 'bg-green-500'
        },
        {
        label: 'Economia média',
        value: '32%',
        color: 'bg-white'
        },
        {
        label: 'Score financeiro',
        value: 'Ótimo',
        color: 'bg-zinc-500'
        }
    ];

}