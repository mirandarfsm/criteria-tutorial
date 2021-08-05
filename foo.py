import csv
actors = []
movies = []
actors_movies = []
a = []
index_actors = 0
index_movies = 0
with open('film.csv', newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=';')
    next(spamreader)
    next(spamreader)
    for row in spamreader:
        # print(row)
        if(row[4] and row[4] not in a):
            actors.append([index_actors,row[4]])
            a.append(row[4])
            am = [index_movies,index_actors]
            actors_movies.append(am)
            index_actors+=1
        else: 
            for i in actors:
                if(row[4] == i[1]):
                    am = [index_movies,i[0]]
                    actors_movies.append(am)

        if(row[5] and row[5] not in a):
            actors.append([index_actors,row[5]])
            a.append(row[5])
            am = [index_movies,index_actors]
            actors_movies.append(am)
            index_actors+=1
        else: 
            for i in actors:
                if(row[5] == i[1]):
                    am = [index_movies,i[0]]
                    actors_movies.append(am)

        if(row[6] and row[6] not in a):
            actors.append([index_actors,row[6]])
            a.append(row[6])
            am = [index_movies,index_actors]
            actors_movies.append(am)
            index_actors+=1
        else: 
            for i in actors:
                if(row[6] == i[1]):
                    am = [index_movies,i[0]]
                    actors_movies.append(am)
        t = {
            'ano': int(row[0]),
            'titulo': row[2],
            'categoria': row[3].upper(),
            'populariedade': int(row[7]) if row[7] else None,
            'premiacao': True if row[8] == 'Yes' else False
        }
        movies.append([index_movies, int(row[0]), row[2], row[3].upper(), int(row[7]) if row[7] else None, True if row[8] == 'Yes' else False])
        index_movies+=1

with open('movie.csv', 'w', newline='') as f:
        writer = csv.writer(f, delimiter=';')
        writer.writerows(movies)

with open('actor.csv', 'w', newline='') as f:
        writer = csv.writer(f, delimiter=';')
        writer.writerows(actors)


with open('actor_movie.csv', 'w', newline='') as f:
        writer = csv.writer(f, delimiter=';')
        writer.writerows(actors_movies)
# print(a)