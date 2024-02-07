const express = require('express')
const bodyParser = require('body-parser');
const path = require('path');
const app = express()
const routes = require('./src/routes')

const staticPath = path.join(__dirname, 'public');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req, res) => {
    res.send("Hello from sever!!!")
})

app.use('/api/auth', routes.auth)
app.use('/api/user', routes.user)
app.use('/api/sign', routes.sign)
app.use('/api/position', routes.position)

app.use(express.static(staticPath));

module.exports = app