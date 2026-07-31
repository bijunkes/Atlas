<p align="center">
  <img src="./assets/logo-atlas-completa.png" alt="Tela inicial do Atlas" width="400"/>
</p>

> Sistema de gerenciamento de finanças pessoais desenvolvido para auxiliar no controle, organização e acompanhamento da vida financeira.

## Sobre o projeto

O Atlas é uma aplicação web de gerenciamento financeiro pessoal, desenvolvida com o objetivo de facilitar o controle de receitas, despesas e planejamento financeiro.

Este projeto está sendo desenvolvido para aprofundar meus conhecimentos em **Angular** e **Spring Boot**, aplicando conceitos aprendidos anteriormente em projetos menores em uma aplicação mais completa e próxima de um ambiente real.

Além de fazer parte do meu portfólio profissional, o Atlas também será uma ferramenta utilizada por mim no dia a dia, unindo aprendizado prático e uma necessidade real.

---

## Objetivos do projeto

- Aprofundar conhecimentos em Angular e Spring Boot.
- Aplicar conceitos de arquitetura de software e boas práticas de desenvolvimento.
- Construir uma aplicação full stack próxima de um ambiente real.
- Criar uma ferramenta pessoal para organização financeira.

---

## Funcionalidades

### Autenticação

- [x] Cadastro e Login de usuários
- [x] Autenticação utilizando JWT
- [x] Criptografia de senha com BCrypt
- [x] Controle de acesso com Spring Security

### Controle financeiro

- [ ] Cadastro de receitas
- [ ] Cadastro de despesas
- [ ] Categorias financeiras
- [ ] Histórico de movimentações

### Dashboard

- [ ] Resumo financeiro
- [ ] Gráficos de gastos
- [ ] Indicadores financeiros

### Planejamento

- [ ] Metas financeiras
- [ ] Controle mensal
- [ ] Acompanhamento de evolução financeira

---

## Arquitetura

### Frontend
- Angular
- Tailwind CSS
- TypeScript

### Backend
- Spring Boot
- Spring Security
- JWT
- JPA/Hibernate

### Banco de dados
- SQLite (ambiente de desenvolvimento)
- JPA/Hibernate para persistência de dados

---

## Interface

Em breve serão adicionadas imagens/gifs da aplicação e demonstrações das principais telas.

---

## Decisões técnicas

### Autenticação JWT

Foi escolhido JWT para permitir uma autenticação stateless, onde o backend não precisa armazenar sessões dos usuários.

### Arquitetura em camadas

A separação entre Controller, Service e Repository foi utilizada para organizar responsabilidades e facilitar manutenção e evolução do projeto.

### Componentização no Angular

Os componentes foram estruturados buscando reutilização e melhor organização da interface.

---

## Diário de Desenvolvimento

### 14/07/2026 — Estruturação inicial do projeto

#### Arquitetura

- Definição da arquitetura geral da aplicação.
- Escolha da stack principal:
  - **Frontend:** Angular
  - **Backend:** Spring Boot
  - **Banco de dados:** SQLite
- Planejamento da separação de responsabilidades entre camadas:
  - Controllers
  - Services
  - Repositories
  - Entities
  - DTOs

#### Design

- Criação da identidade visual do Atlas.
- Definição do Design System inicial:
  - Background: `#09090B`
  - Surface: `#18181A`
  - Border: `#27272A`
  - Text: `#FFFFFF`
  - Primary: `#22C55E`
  - Hover: `#16A34A`
- Escolha da tipografia:
  - Inter
  - Geist
  - Manrope

#### Banco de Dados

- Modelagem inicial das entidades.
- Criação da estrutura base para usuários.
- Planejamento da persistência de dados utilizando JPA/Hibernate.

### 15/07/2026 — Sistema de autenticação

#### Cadastro e Login

- Implementação da entidade `Usuario`.
- Criação do fluxo de cadastro.
- Criação do fluxo de login.
- Desenvolvimento dos DTOs:
  - `RegisterDTO`
  - `LoginDTO`

#### Autenticação JWT

- Implementação da autenticação utilizando JWT.
- Criação do serviço responsável pela geração e validação dos tokens.
- Configuração do Spring Security.
- Implementação do `SecurityConfig`.
- Configuração da aplicação para trabalhar com sessões stateless.

#### Comunicação Frontend + Backend

- Configuração do CORS.
- Integração inicial entre Angular e Spring Boot.
- Testes de comunicação entre cliente e API.


### 16/07/2026 — Desenvolvimento da Landing Page

#### Frontend

- Criação da primeira versão da Landing Page do Atlas, utilizada como protótipo para validar a identidade visual e estrutura inicial da aplicação.

### 17/07/2026 — Melhorias na autenticação

- Implementação da criptografia de senhas utilizando BCrypt.
- Implementação da validação de autenticação através do filtro JWT (JwtAuthenticationFilter).
- Criação de rota protegida para testes de autenticação.

### 22/07/2026 — Tratamento de erros

#### Backend

- Criação de exceções personalizadas para representar diferentes cenários de erro da aplicação.
- Implementação do `GlobalExceptionHandler` para centralizar e padronizar o tratamento das exceções da API.
- Criação do `ErrorResponseDTO` para definir um formato consistente nas respostas de erro.
- Implementação do `JwtAuthenticationEntryPoint` para tratar falhas de autenticação.

Tratamentos implementados:

- Credenciais inválidas durante o login.
- Tentativa de cadastro com e-mail já existente.
- Recursos não encontrados.
- Acesso não autorizado.
- Tokens JWT inválidos.
- Tokens JWT expirados.

#### Frontend

- Integração com as respostas de erro retornadas pela API.
- Implementação do tratamento de erros de autenticação.
- Preparação da estrutura de notificações utilizando Toast.

### 25/07/2026 — Documentação do projeto

#### README.md

- Criação da documentação inicial do Atlas.
- Estruturação do README com informações sobre:
  - Objetivos do projeto;
  - Funcionalidades implementadas e planejadas;
  - Arquitetura da aplicação;
  - Decisões técnicas;
  - Diário de desenvolvimento.

### 26/07/2026 — Integração da autenticação no frontend

#### Backend

- Criação da rota `/auth/me` para recuperar os dados do usuário autenticado.
- Integração com o Spring Security para identificação do usuário através do JWT.

#### Frontend

- Estruturação do `AuthService` para gerenciamento da autenticação.
- Implementação do armazenamento e recuperação dos tokens JWT.
- Validação da expiração do token utilizando `jwt-decode`.
- Integração com a rota `/auth/me` para carregar o usuário autenticado.
- Criação do `authInterceptor` para envio automático do token nas requisições protegidas.
- Implementação do `authGuard` para proteção das rotas privadas.
- Atualização do fluxo de login para persistir a sessão e redirecionar o usuário autenticado.

### 27/07/2026 — Gerenciamento de estado do usuário

#### Frontend

- Criação do `UserStateService` utilizando Angular Signals para gerenciamento do usuário autenticado.
- Integração do estado global do usuário com o `AuthService`.
- Implementação da restauração automática da sessão ao iniciar a aplicação.
- Atualização do logout para limpar o estado do usuário.
- Remoção do armazenamento do usuário no `localStorage`, mantendo apenas os tokens persistidos.
- Integração do usuário autenticado no Dashboard através do `UserStateService`.
- Implementação e validação do fluxo de logout na área protegida.

### 28/07/2026 — Implementação da camada de domínio financeiro

#### Backend

- Criação das entidades financeiras e relacionamentos JPA.
- Separação dos serviços de autenticação e usuário.
- Implementação dos DTOs e repositories de contas, categorias e transações.
- Criação da TransacaoService com CRUD, exclusão lógica e filtros.
- Preparação do backend para integração com o Angular.

### 29/07/2026 — Implementação do layout e navegação da aplicação

#### Frontend

- Criação do `SidebarComponent` com navegação responsiva para desktop e mobile.
- Implementação do menu hamburger, overlay e fechamento automático por rota.
- Adição do modo recolhido/expandido com persistência no `localStorage`.
- Implementação da área do usuário no sidebar com nome, e-mail, iniciais e logout.
- Integração do sidebar com o UserStateService para atualização dinâmica das informações do usuário autenticado.
- Implementação da página de perfil.
- Criação do formulário de edição de perfil.

### 30/07/2026 — Implementação de recuperação de senha e melhorias no perfil

#### Frontend

- Implementação do fluxo de alteração de senha pelo perfil do usuário.
- Integração da recuperação de senha com envio de e-mail.
- Adição de estados de loading e feedbacks visuais nas ações de autenticação.
- Ajustes na página de redefinição de senha e na área de segurança do perfil.
- Implementação da alteração e remoção da imagem de perfil.

#### Backend

- Implementação dos endpoints de recuperação e redefinição de senha.
- Criação do fluxo de token temporário para alteração de senha.
- Implementação do envio de e-mail para recuperação de acesso.
- Integração com o Cloudinary para armazenamento em nuvem das imagens de perfil.
- Implementação do gerenciamento das imagens dos usuários, incluindo upload e remoção de arquivos.

### 31/07/2026 — Implementação de autenticação com Google e gerenciamento de senhas

#### Frontend

- Implementação do gerenciamento de diferentes tipos de autenticação (`LOCAL`, `GOOGLE` e `GOOGLE_AND_LOCAL`).
- Criação do modal para usuários Google definirem uma senha de acesso por e-mail.
- Adição de validações, confirmação de senha e feedbacks visuais no fluxo de criação de senha.
- Ajustes na área de segurança do perfil conforme o método de login do usuário.
- Organização do componente de perfil e padronização dos métodos.

#### Backend

- Implementação do suporte aos provedores de autenticação Google e Local.
- Criação do fluxo de definição de senha para usuários autenticados via Google.
- Atualização automática para `GOOGLE_AND_LOCAL` após criação de senha.
- Ajustes no login e recuperação de senha para lidar com diferentes tipos de conta.
- Validação de expiração dos tokens de recuperação de senha.

---

## Desenvolvido por

Bianca Junkes Rech