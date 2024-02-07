require('dotenv').config()

const http = require('http')
const app = require('./app')
const port = process.env.PORT || 8000
const domainname = process.env.DOMAINNAME
const connection = require('./src/config/connectDB')

const server = http.createServer(app)


server.listen(port)

