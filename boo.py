import csv
import datetime
import random
from random import randint

actors = []
file = 'actor.csv'
with open(file, newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=';')
    for row in spamreader:
        date=datetime.date(randint(1940,2010), randint(1,12),randint(1,28))
        row.append(str(date))
        actors.append(row)
# print(actors)

with open(file, 'w', newline='') as f:
        writer = csv.writer(f, delimiter=';')
        writer.writerows(actors)



def random_date(start, end):
    """Generate a random datetime between `start` and `end`"""
    return start + datetime.timedelta(
        # Get a random amount of seconds between `start` and `end`
        seconds=random.randint(0, int((end - start).total_seconds())),
    )
