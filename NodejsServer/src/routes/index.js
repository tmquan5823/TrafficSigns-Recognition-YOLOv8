const auth = require('./auth.route')
const user = require('./user.route')
const sign = require('./sign.route')
const position = require('./position.route')

module.exports = {
    auth: auth,
    user: user,
    sign: sign,
    position: position
}