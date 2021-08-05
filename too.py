import requests

headers = {
    'x-rapidapi-key': "f3aba96205msh145c5b00402c06bp156a5ejsn6642ab69a74b",
    'x-rapidapi-host': "data-imdb1.p.rapidapi.com"
    }

for i in range(54047,9999999):
    url = "https://data-imdb1.p.rapidapi.com/movie/id/tt"
    url += '{0:07}'.format(i) + '/'
    response = requests.request("GET", url, headers=headers)
    print('{0:07}'.format(i) + ': ' + response.text)