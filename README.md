# MapaBike v0.3

Protótipo Android para navegação de bicicleta com foco em entregas.

## Recursos desta versão
- Endereço da entrega fica visível durante toda a navegação.
- Avisos ao chegar a 250 m e 60 m do destino.
- Rota de bicicleta com OSRM.
- Amostragem de elevação e marcadores: laranja = subida, vermelho = subida forte, verde = descida, cinza = plano.
- GPS do celular.
- Recebe destinos por intents Android comuns (`geo:`, `google.navigation:` e alguns links com endereço), permitindo que apps de entrega/navegação compatíveis ofereçam o MapaBike como opção.

## Compilação online
O repositório inclui `.github/workflows/build-apk.yml`. No GitHub, abra **Actions**, selecione **MapaBike - gerar APK**, toque em **Run workflow** e, quando terminar, baixe o artefato `MapaBike-debug-apk`.

## Observação
É um protótipo de teste. Os serviços públicos de mapa/rota/elevação podem ter limites e não devem ser tratados como infraestrutura de produção.
