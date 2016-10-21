# ShortID
Two services using shortid

## Description
Service A uses a UUID to uniquely identify entries (example 70f525c1-1e93-483c-9c92-b9459020462e).
But service B uses an identifier of 20 alphanumerical characters. So service A needs some functionality
to convert/match the UUID to service B's schema.

### Problem
The UUID is 128bit long, mapping that to the 20 characters of the service B identifier would need
6.4 bits per character. That means 2^6.4 = 85 different figures!
Alphanumeric is normal defined as A-Z + a-z + 0-9, that's 62 figures only. So the UUID can't be mapped
on 20 alphanumeric characters, without loosing information.

### Solution 1
62 is about 2^5.96, that means we would only get 5.96 bit per character, that's 119 bit for 20
characters.
So that solution will lead to a loss of 9 bits which increases the chance of a collision! This means
a higher chance, that 2 random UUIDs have the same representation in the 20 alphanumeric character
representation.

### Solution 2
Extend the meaning of alphanumeric so there are more figures to map all bits.The ASCII code has
usable figures from position 33 to 126, that means one can choose his "alphabet" out of those 94
different figures.

As this is the only safe way, this project chooses solution 2.