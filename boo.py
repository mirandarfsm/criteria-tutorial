import csv
actors = []
with open('actor.csv', newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=';')
    for row in spamreader:
        name = ''
        a = row[1].split(', ')
        if len(a) > 1:
            name = a[1]
            name += ' '
        name += a[0]
        if len(a) > 2: 
            name += ' '
            name += a[2]
        actors.append([row[0],name])
# print(actors)

with open('actor.csv', 'w', newline='') as f:
        writer = csv.writer(f, delimiter=';')
        writer.writerows(actors)