const axios = require('axios');

const blobTransfer = async (url) => {
    const response = await axios.get(url, { responseType: 'arraybuffer' });
    return Buffer.from(response.data, 'binary');
}

module.exports = { blobTransfer }