function visualization()
{
    'use strict'
    feather.replace({ 'aria-hidden': 'true' })

    var ctx = document.getElementById('myChart')

    //Set up the bar graph
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [
                'Murder',
                'Rape',
                'Robbery',
                'Assault',
                'Burglary',
                'Larceny',
                'Auto Theft',
                'Arson',
                'Shooting'
            ],
            datasets: [{
                data: [
                    parseInt(document.getElementById('type1').innerText),
                    parseInt(document.getElementById('type2').innerText),
                    parseInt(document.getElementById('type3').innerText),
                    parseInt(document.getElementById('type4').innerText),
                    parseInt(document.getElementById('type5').innerText),
                    parseInt(document.getElementById('type6').innerText),
                    parseInt(document.getElementById('type7').innerText),
                    parseInt(document.getElementById('type8').innerText),
                    parseInt(document.getElementById('type9').innerText)
                ],
                backgroundColor: [
                    'rgb(54,162,235)',
                    'rgb(169,227,119)',
                    'rgb(220,119,227)',
                    'rgb(139,119,227)',
                    'rgb(215,167,95)',
                    'rgb(231,110,173)',
                    'rgb(164,164,164)',
                    'rgb(232,236,169)',
                    'rgb(175,251,255)'
                ]
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false
            }
        }
    })
}