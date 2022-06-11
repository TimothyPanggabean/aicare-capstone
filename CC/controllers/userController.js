const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const { v4: uuidv4 } = require('uuid')
const { client } = require('../config/connectdb')

const register = async (req, res) => {
    const {
        name,
        email,
        password,
        number,
    } = req.body

    try {
        if(!name || !email || !password || !number ) {
            throw new Error('Please fill all the fields.')
        }

        const { rows } = await client.query(`SELECT * FROM users WHERE email='${email}'`)

        if(rows.length === 1) {
            throw new Error('User with that email already exists.')
        }

        const salt = await bcrypt.genSalt(10)
        const hashed = await bcrypt.hash(password, salt)
        const id = 'user-'+ uuidv4()

        //TODO: tambahin user baru
        //error handling untuk data yang invalid
        client.query(`INSERT INTO users VALUES (
            '${id}', '${name}', '${email}','${hashed}', ${number}
        )`, (err, result) => {
            if(err) {
                res.status(400).json({
                    error: true,
                    message: err.message
                })
            }
            res.status(201).json({
                error: false,
                message: `New user ${name} successfully created!`,
                token: generateToken(id)
            })
        })

    } catch(err) {
        res.status(400).json({
            error: true,
            message: err.message
        })
    }
}

const login = async (req, res) => {
    const { email, password } = req.body

    try {
        if(!email || !password) {
            throw new Error('Please fill all the fields.')
        }

        const { rows } = await client.query(`SELECT * FROM users WHERE email='${email}'`)

        if(rows.length === 0) {
            throw new Error('User does not exist.')
        }

        const user = rows[0]
        
        if(bcrypt.compare(password, user.password)){
            res.status(201).json({
                error: false,
                message: 'Login successful.',
                LoginInfo: {
                    id: user.id,
                    name: user.name,
                    email: user.email,
                    number: user.number,
                    token: generateToken(user.id)
                }
            })
        } else {
            res.status(400)
            throw new Error('Invalid credentials')
        }

    } catch(err) {
        res.status(400).json({
            error: true,
            message: err.message
        })
    }
}

const generateToken = (id) => {
    return jwt.sign({ id }, process.env.JWT_SECRET, {
        expiresIn: '30d',
    })
}

module.exports = { register, login }