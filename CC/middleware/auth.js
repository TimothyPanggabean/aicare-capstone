const jwt = require('jsonwebtoken')
const { client } = require('../config/connectdb')

const authorize = async (req, res, next) => {
    let token
    if(req.headers.authorization && req.headers.authorization.startsWith('Bearer')){
        try {
            token = req.headers.authorization.split(' ')[1]
            const decoded = jwt.verify(token, process.env.JWT_SECRET)

            const { rows } = await client.query(`SELECT * from users where id = '${decoded.id}'`)
            req.user = rows[0]
            next()

        } catch (err) {
            res.status(401).json({
                error: true,
                message: 'User not authorized.'
            })
        }
    }

    if(!token) {
        res.status(401).json({
            error: true,
            message: 'User not authorized, no token received.'
        })
    }
}

module.exports = { authorize }