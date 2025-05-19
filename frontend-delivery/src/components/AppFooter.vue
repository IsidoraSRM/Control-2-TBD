<script>
export default {
    name: 'AppFooter',
};
</script>

<template>
    <footer class="footer">
        <div class="footer-left">
            <span class="footer-text">© 2025 DocDelivery. Grupo 3 TBD.</span>
        </div>
        <div class="footer-center">
            <a href="#" class="footer-link">Políticas de Privacidad</a>
            <a href="#" class="footer-link">Términos de Servicio</a>
            <a href="#" class="footer-link">Contáctanos</a>
        </div>
        <div class="footer-right">
            <a href="#" class="footer-icon" aria-label="Instagram">
                <i class="fab fa-instagram"></i>
            </a>
        </div>
    </footer>
</template>

<style scoped>
.footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #125A6C;
    color: #E2DCD2;
    padding: 5px 20px;
    font-size: 12px;
    height: 40px; /* Altura total incluyendo íconos */
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    box-shadow: 0 -1px 4px rgba(0, 0, 0, 0.1);
}

.footer-left,
.footer-center,
.footer-right {
    display: flex;
    align-items: center;
}

.footer-text {
    font-weight: 400;
}

.footer-center {
    gap: 15px;
}

.footer-link {
    color: #E2DCD2;
    text-decoration: none;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 4px;
    transition: background-color 0.2s ease, color 0.2s ease;
}

.footer-link:hover {
    background-color: #F5C448;
    color: #125A6C;
}

.footer-icon {
    color: #E2DCD2;
    font-size: 14px;
    width: 24px;
    height: 24px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.1);
    transition: background-color 0.2s ease, transform 0.2s ease;
}

.footer-icon:hover {
    background-color: #F5C448;
    color: #125A6C;
    transform: scale(1.1);
}

@media (max-width: 600px) {
    .footer {
        flex-direction: column;
        height: auto;
        gap: 5px;
        padding: 8px 10px;
        text-align: center;
    }
    .footer-center {
        flex-wrap: wrap;
        justify-content: center;
    }
}
</style>
