import csv
a=[]
with open('film.csv', newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=';')
    next(spamreader)
    next(spamreader)
    for row in spamreader:
        if(row[3].upper() and row[3].upper() not in a):
            a.append(row[3].upper())
print(','.join(a))
# COMEDY,HORROR,ACTION,DRAMA,MYSTERY,SCIENCE_FICTION,MUSIC,WAR,WESTERNS,WESTERN,SHORT,ADVENTURE,CRIME,ROMANCE,FANTASY