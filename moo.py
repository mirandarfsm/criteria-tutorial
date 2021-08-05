import requests
import csv


headers = {
    'x-rapidapi-key': "f3aba96205msh145c5b00402c06bp156a5ejsn6642ab69a74b",
    'x-rapidapi-host': "data-imdb1.p.rapidapi.com"
    }

with open('actor.csv', newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=';')
    for row in spamreader:
        url = "https://data-imdb1.p.rapidapi.com/actor/imdb_id_byName/"
        url += row[1]+ '/'
        response = requests.request("GET", url, headers=headers)
        print(row[1] + ': ' + response.text)
