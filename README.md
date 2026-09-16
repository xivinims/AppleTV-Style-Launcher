# Apple TV Style Launcher for Android TV

Launcher completo e limpo inspirado na Apple TV + estética **Night do iOS 18**.

## Funcionalidades

- Grid de apps estilo Apple TV (ícones arredondados + animação de foco com scale)
- Relógio no canto superior direito
- **Suspensão de tela (Screensaver)** após 5 minutos de inatividade (configurável)
- Tema escuro profundo estilo iOS 18 Night
- Tela de Configurações completa:
  - Tempo de timeout do screensaver
  - Número de colunas (3 a 7)
  - Mostrar/esconder nomes dos apps
  - Modo Noite iOS 18
- Lista todos os apps instalados (incluindo sideloaded)
- Pronto para ser definido como launcher padrão (HOME + LEANBACK_LAUNCHER)
- DreamService para aparecer nas opções de Screen Saver do sistema

## Como buildar

1. Abra o projeto no Android Studio
2. Sync Gradle
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. O APK estará em `app/build/outputs/apk/debug/` ou `release/`

## Instalação na TV

```bash
adb connect IP_DA_TV:5555
adb install -r app-debug.apk
```

Depois pressione o botão Home e escolha este launcher como padrão.

## Personalização

Tudo fica em **Configurações** (ícone no topo direito da Home).

---

Feito com ❤️ para deixar sua TV bonita.
