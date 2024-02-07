const { showAllSigns, addSign } = require("../CRUD/sign")
const axios = require('axios')
require('dotenv').config()

const getListSign = async (req, res) => {
    try {
        const listSign = await showAllSigns()
        res.status(200).json({
            listSign: listSign
        })
    } catch (error) {
        // In ra lỗi từ Sequelize
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Show List Sign!!!",
            error: error.message,
        });
    }
}


const regconizeSign = async (req, res) => {
    try {
        // const pathToPython = "d:\\PBL4\\Detection.py"
        // let options = {
        //     args: [req.body.image]
        // }
        // const results = await PythonShell.run(pathToPython, options)
        // console.log(results)
        // res.status(200).json({
        //     message: "Detection succesfully!!!",
        //     // image: results[0],
        //     name: results[2]
        // })
        const dataToSend = {
            image: req.body.image
        }
        axios.post(process.env.SV_FLASK, dataToSend)
            .then(response => {
                res.status(200).json({
                    name: response
                })
            })
            .catch(error => {
                console.error(error.message)
            })
    } catch (error) {
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Detection Sign!!!",
            error: error.message,
        });
    }
}


module.exports = {
    getListSign,
    regconizeSign
}