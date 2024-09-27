// Variable de visibilidad para el menú de navegación al cargar la página
let navVisible = true;


// Evento que se ejecuta cuando el contenido del documento se ha cargado completamente
    // Esta función asegura que todos los elementos del DOM estén disponibles para su manipulación, permitiendo realizar ajustes iniciales como la configuración de la barra de navegación y el margen del contenido principal.
    document.addEventListener('DOMContentLoaded', function () {

    const navbar = document.getElementById('navbar');
    const mainContent = document.getElementById('main-content');

    navbar.classList.add('nav-visible');
    mainContent.style.marginLeft = '70px';


// Función para Mostrar / Ocultar la Barra de Navegación
    // Función que alterna la visibilidad de la barra de navegación, cambiando su clase y ajustando el margen del contenido principal según su estado (visible u oculto).
    function toggleNav() {
        navVisible = !navVisible; 

        if (navVisible) {
        navbar.classList.remove('nav-hidden');
        navbar.classList.add('nav-visible');
        mainContent.style.marginLeft = '70px'; 
        } else {
        navbar.classList.remove('nav-visible');
        navbar.classList.add('nav-hidden');
        mainContent.style.marginLeft = '220px'; 
        }
    }

    navbar.addEventListener('mouseenter', toggleNav);
    navbar.addEventListener('mouseleave', toggleNav);
    });


// Gráfico de Línea
    // Se obtiene el contexto del lienzo del gráfico de línea y se crea el gráfico usando la librería Chart.js
    const ctxLine = document.getElementById('lineChart').getContext('2d');
    const lineChart = new Chart(ctxLine, {
        type: 'line',
        data: {
            labels: Array.from({ length: 30 }, (_, i) => `Dia ${i + 1}`),
            datasets: [{
                label: 'Abertos',
                data: Array.from({ length: 30 }, () => Math.floor(Math.random() * 100)), 
                borderColor: 'darkred',   
                fill: false 
            }]
        }
    });


// Gráfico de Pastel
    // Se obtiene el contexto del lienzo del gráfico de pastel y se crea el gráfico
    var ctxPie = document.getElementById('pieChart').getContext('2d');
    var pieChart = new Chart(ctxPie, {
        type: 'pie',
        data: {
            labels: ['USA', 'Canada', 'UK', 'Germany', 'France', 'China'],
            datasets: [{
                data: [3000, 2500, 2000, 1500, 1000, 500],
                backgroundColor: ['rgba(255, 255, 255, 0.80', 'rgba(255, 255, 255, 0.50', 'gray', 'rgba(127, 127, 127, 0.25',  'rgba(0, 0, 0, 0)', 'light'] 
            }]
        }
    });


// Gráfico de Barras
    // Se obtiene el contexto del lienzo del gráfico de barras y se crea el gráfico
    const ctxBar = document.getElementById('expensesChart').getContext('2d');
    const barChart = new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: ['Jan/Feb', 'Mar/Apr', 'May/Jun', 'Jul/Aug', 'Sep/Oct', 'Nov/Dec'],
            datasets: [{
                label: 'Expenses',
                data: [9000, 6000, 8000, 6000, 4000, 2000],
                backgroundColor: 'darkred' 
            }]
        }
    });


// Gráfico Mixto entre Radar y Polar
    // Se obtiene el contexto del lienzo del gráfico mixto y se crea el gráfico
    const ctxMixedPolarArea = document.getElementById('PolarAreaChart').getContext('2d');
    const mixedChartPolarArea = new Chart(ctxMixedPolarArea, {
        type: 'radar', 
        data: {
            labels: ["Referrals", "People", "Whats", "WebSite", "Social Media" ],
            datasets: [
                {
                    label: "Growth",
                    data: [100, 100, 100, 100, 100],
                    backgroundColor: "rgba(255, 0, 0, 0.25)", 
                    borderColor: 'rgba(255, 0, 0, 1)', 
                    borderWidth: 1 
                },
                {
                    type: 'polarArea', 
                    label: "Complaints",
                    data: [60, 70, 80, 90, 100],
                    backgroundColor: [
                        'rgba(0, 0, 255, 1)',
                        'rgba(0, 0, 205, 1)',
                        'rgba(0, 0, 155, 1)',
                        'rgba(0, 0, 105, 1)',
                        'rgba(0, 0, 55, 1)'
                    ],
                    borderColor: [
                        'rgba(0, 0, 255, 1)',
                        'rgba(0, 0, 205, 1)',
                        'rgba(0, 0, 155, 1)',
                        'rgba(0, 0, 105, 1)',
                        'rgba(0, 0, 55, 1)'
                    ],
                    borderWidth: 1,
                    x: 0.5,
                    y: 0.5, 
                    radius: 0.7 
                }
            ]
        },
        options: {
            plugins: {
                title: {
                    display: true,
                    text: "Growth y Complaints" 
                }
            },
            scales: {
                r: {
                    angleLines: {
                        display: true 
                    },
                    suggestedMin: 0, 
                    suggestedMax: 100
                }
            }
        }
    });


// Gráfico de Barras
    // Se obtiene el contexto del lienzo del gráfico de barras y se crea el gráfico
    const ctxPerformance = document.getElementById('incomeChart').getContext('2d');
    const performanceChart = new Chart(ctxPerformance, {
        type: 'bar', 
        data: {
            labels: ['Jan/Feb', 'Mar/Apr', 'May/Jun', 'Jul/Aug', 'Sep/Oct', 'Nov/Dec'],
            datasets: [{
                label: 'Income', 
                data: [2000, 4000, 6000, 8000, 6000, 9000], 
                backgroundColor: 'green' 
            }]
        }
    });


// Gráfico de Doughnut
    // Se obtiene el contexto del lienzo del gráfico de doughnut y se crea el gráfico
    const ctxFeedback = document.getElementById('feedbackChart').getContext('2d');
    const feedbackChart = new Chart(ctxFeedback, {
        type: 'doughnut',
        data: {
            labels: ['Medium', 'Negative', 'Neutral', 'Regular', 'Good', 'Excelent'],
            datasets: [{
                data: [50, 100, 150, 200, 250, 300], 
                backgroundColor: ['light', 'rgba(0, 0, 0, 0)', 'rgba(127, 127, 127, 0.25', 'gray', 'rgba(255, 255, 255, 0.50', 'rgba(255, 255, 255, 0.80'] 
            }]
        }
    });


// Gráfico de Línea
    // Se obtiene el contexto del lienzo del gráfico de línea y se crea el gráfico
    const ctxSales = document.getElementById('sales1Chart').getContext('2d');
    const salesChart = new Chart(ctxSales, {
        type: 'line', 
        data: {
            labels: ['January', 'February', 'March', 'April', 'May', 'June'],
            datasets: [{
                label: 'Sales', 
                data: [0, 600, 200, 800, 400, 1000], 
                borderColor: 'orange',
                fill: true, 
                borderWidth: 1, 
            }]
        }
    });


// Gráfico Polar
    // Se obtiene el contexto del lienzo del gráfico polar y se crea el gráfico
    const ctxDoughnut = document.getElementById('complaintsChart').getContext('2d');
    const doughnutChart = new Chart(ctxDoughnut, {
        type: 'polarArea', 
        data: {
            labels: ["Referrals", "Directly", "Whats", "WebSite", "Social Media"],
            datasets: [{
                label: 'Complaints', 
                data: [60, 70, 80, 90, 100], 
                backgroundColor: [
                    'rgba(0, 0, 255, 1)',
                    'rgba(0, 0, 205, 1)',
                    'rgba(0, 0, 155, 1)',
                    'rgba(0, 0, 105, 1)',
                    'rgba(0, 0, 55, 1)'
                ], 
                borderColor: [
                    'rgba(0, 0, 255, 1)',
                    'rgba(0, 0, 205, 1)',
                    'rgba(0, 0, 155, 1)',
                    'rgba(0, 0, 105, 1)',
                    'rgba(0, 0, 55, 1)'
                ], 
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                r: {
                    beginAtZero: true 
                }
            }
        }
    });


// Gráfico Mixto Entre Líneas y Barras
    // Se obtiene el contexto del lienzo del gráfico mixto y se crea el gráfico
    const ctxMixedLinesBar = document.getElementById('LinesBarChart').getContext('2d');
    const mixedChartLinesBar = new Chart(ctxMixedLinesBar, {
        type: 'bar', 
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                type: 'line',
                label: 'Income',
                data: [0, 3000, 6000, 9000, 6000, 3000, 6000, 9000, 6000, 3000, 6000, 10000], 
                borderColor: 'rgba(0, 0, 111, 1)', 
                backgroundColor: 'rgba(0, 0, 123, 1)', 
                fill: false 
            }, {
                type: 'bar',
                label: 'Expenses', 
                data: [1500, 3000, 4500, 6000, 7500, 9000, 6000, 3000, 6000, 9000, 6000, 4000], 
                backgroundColor: "rgba(200, 0, 0, 0.50)", 
                borderColor: "rgba(200, 0, 0, 0.50)" 
            }]
        }
    });


// Gráfico de Radar
    // Se obtiene el contexto del lienzo del gráfico de radar y se crea el gráfico
    const ctxUserGrowth = document.getElementById('GrowthChart').getContext('2d');
    const userGrowthChart = new Chart(ctxUserGrowth, {
        type: 'radar', 
        data: {
            labels: ["Social Media", "WebSite", "Referrals", "Community", "WhatsApp"],
            datasets: [
                {
                    label: "Growth", 
                    data: [1000, 1000, 1000, 1000, 1000], 
                    backgroundColor: "rgba(200, 0, 0, 0.50)",
                    borderColor: 'darkred', 
                    borderWidth: 1
                }
            ]
        }
    });


// Gráfico de Línea
    // Se obtiene el contexto del lienzo del gráfico de línea y se crea el gráfico
    const ctxGauge = document.getElementById('sales2Chart').getContext('2d');
    const gaugeChart = new Chart(ctxGauge, {
        type: 'line', 
        data: {
            labels: ['July', 'August', 'September', 'October', 'November', 'December'],
            datasets: [{
                label: 'Sales', 
                data: [1000, 400, 800, 200, 600, 0], 
                borderColor: 'orange', 
                borderWidth: 1, 
                fill: true 
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true 
                }
            }
        }
    });