const { client } = require('../config/connectdb')
const { v4: uuidv4 } = require('uuid')

const getAllHospitals = async (req, res) => {
    const query = `SELECT * FROM hospitals;`
    
    client.query(query, (err, result) => {
        if(err) {
            res.status(400).json({
                error: true,
                message: err.message
            })
        }

        const list = result.rows.map(row => ({
                id: row.id,
                hospitalName: row.name,
                lat: row.lat,
                long: row.long
            })
        )

        res.json({
            error: false,
            message: 'Succesfully retrieved all hospitals.',
            listHospital: list
        })
    })
}

const addHospital = async (req, res) => {
    const { name, lat, long } = req.body

    try {
        if(!name || !lat || !long ) {
            throw new Error('Please fill all the fields.')
        }

        const { rows } = await client.query(`SELECT * FROM hospitals WHERE lat=${lat} and long=${long}`)

        if(rows.length === 1) {
            throw new Error('The hospital is already recorded.')
        }
        
        const id = 'hospital-'+ uuidv4()

        client.query(`INSERT INTO hospitals VALUES (
            '${id}', '${name}', '${lat}','${long}')`, (err, result) => {
            if(err) {
                res.status(400).json({
                    error: true,
                    message: err.message
                })
            }
            res.status(201).json({
                error: false,
                message: `New hospital ${name} successfully created!`,
            })
        })

    } catch(err) {
        res.status(400).json({
            error: true,
            message: err.message
        })
    }
}

module.exports = { getAllHospitals, addHospital }