console.log("hi");

const apiKey = "6e4a79cc5a5801c8726909e92ce91139";
const apiUrl = "https://api.openweathermap.org/data/2.5/weather?units=metric&q=";

const searchBox = document.querySelector('.search input');
const searchBtn = document.querySelector('.search button');
const wheatherIcon = document.querySelector('.whether-icon');

async function checkWheather(city) {
    const response = await fetch(apiUrl + city + `&appid=${apiKey}`);

    if(response.status == 404) {
        console.log(response.status);
        console.log("no data");
        document.querySelector(".error").style.display = "block";
        document.querySelector(".whether").style.display = "none";
    }
    else {
        const data = await response.json();
        console.log("DATA availabe");
        console.log(data);

        document.querySelector('.city').innerHTML = data.name;
        document.querySelector('.temp').innerHTML = data.main.temp + "°C";
        document.querySelector('.humidity').innerHTML = data.main.humidity + "%";
        document.querySelector('.wind').innerHTML = data.wind.speed + " km/h";
    
    
        if(data.weather[0].main == "Clouds") {
            wheatherIcon.src = "images/clouds.png"
        }
        if(data.weather[0].main == "Clear") {
            wheatherIcon.src = "images/clear.png"
        }
        if(data.weather[0].main == "Drizzle") {
            wheatherIcon.src = "images/drizzle.png"
        }
        if(data.weather[0].main == "Humidity") {
            wheatherIcon.src = "images/humidity.png"
        }
        if(data.weather[0].main == "Mist") {
            wheatherIcon.src = "images/mist.png"
        }
    
        document.querySelector('.whether').style.display = 'block';
        document.querySelector('.error').style.display = "none";

        console.log("Area: " + data.name);
    }
}


searchBtn.addEventListener('click', ()=>{
    checkWheather(searchBox.value);
});