
db.createCollection("user");


db.createUser({user: "spring", pwd: "admin", roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]})