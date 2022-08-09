# Through Java to realize the mysql database function.
# The data has been read in from the filesystem, and it stored in memory. The folder as a database, the folder files as a table.
# The handler is for the incoming commands which will parse them, perform the specified queries and return the result to the client. 
# The query language are use for this purpose supports the following main types of query:
# USE: changes the database against which the following queries will be run
# CREATE: constructs a new database or table (depending on the provided parameters)
# INSERT: adds a new record (row) to an existing table
# SELECT: searches for records that match the given condition
# UPDATE: changes the existing data contained within a table
# ALTER: changes the structure (columns) of an existing table
# DELETE: removes records that match the given condition from an existing table
# DROP: removes a specified table from a database, or removes the entire database
# JOIN: performs an inner join on two tables (returning all permutations of all matching records)

# SQL keywords are case insensitive and my query interpreter could identify any errors in the construction of queries (for example queries not conforming to the BNF or queries that include unknown database / table / attribute identifier). The response returned from the server will begin with one of the following two "status" tags:

# [OK] for valid and successful queries, followed by the results of the query.
# [ERROR] if the query is invalid, followed by a suitable human-readable message that provides information about the nature of the error.
