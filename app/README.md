# RestAppStore

Una aplicación Android para registrar y gestionar softwares usando una API REST. Cuenta con un splash screen personalizado, un formulario de registro con diálogo de confirmación, y soporte para listar softwares.

## Características
- **Splash Screen**: Muestra un logo personalizado durante 2 segundos al iniciar.
- **Formulario de Registro**: Ingresa nombre, espacio en MB, versión y precio de un software.
- **Diálogo de Confirmación**: Pregunta si deseas registrar antes de guardar.
- **Botón de Ejemplo**: Llena el formulario con datos de prueba (Antivirus Pro, 150 MB, 2.3.1, $29.99).
- **API REST**: Conecta con una API para almacenar los datos.

## Tecnologías
- Android SDK
- Java
- Volley (para peticiones HTTP)
- API REST: https://rest-api-software-production-57a6.up.railway.app/api/softwares

## Paso a Paso
1. **Clonar el repositorio**:
    - Abre una terminal y ejecuta:
      ```bash
      git clone https://github.com/tefii-14/RestAppStore.git