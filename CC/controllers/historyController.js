const { v1: uuidv1 } = require('uuid')
const firebase = require('../config/firestore')

const db = firebase.firestore()

const getHistory = async (req, res) => {
    const historyRef = db.collection('history').where('userid', '==', req.user.id)
    try {
        const snapshot = await historyRef.get()
        var records = []

        snapshot.forEach((doc) => {
            records.push(doc.data())
        })

        res.json({
            error: false,
            message: 'History list successfully obtained.',
            listHistory: records
        })

    } catch (err) {
        res.status(400).json({
            error: true,
            message: err.message
        })
    }
}

const addHistory = async (req, res) => {
    const { label, percentage } = req.body
    const userid = req.user.id

    const id = 'record-' + uuidv1() 
    const recordRef = db.collection('history').doc(id)

    try {
        if(!label || !percentage) {
            throw new Error('Please fill all the fields.')
        }

        let newRecord

        if(req.file !== undefined) {
            const imageURL = req.file.cloudStoragePublicUrl
            newRecord = {
                id,
                userid,
                outputlabel: label,
                outputpercentage: percentage,
                date: new Date(),
                imageURL
            }
        } else {
            newRecord = {
                id,
                userid,
                outputlabel: label,
                outputpercentage: percentage,
                date: new Date(),
            }
        }

        await recordRef.set(newRecord)

        res.json({
            error: false,
            message: 'Record successfully added to history.',
            newRecord
        })

    } catch (err) {
        res.status(400).json({
            error: true,
            message: err.message
        })
    }
}

module.exports = {
    getHistory, addHistory
}