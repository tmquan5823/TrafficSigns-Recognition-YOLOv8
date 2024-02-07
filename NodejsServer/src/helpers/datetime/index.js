const { DateTime } = require('luxon');

function getCurrentDateTime() {
    const vietnamTime = DateTime.now().setZone('Asia/Ho_Chi_Minh')
    return vietnamTime.toFormat('yyyy-MM-dd HH:mm:ss')
}

function toLocaleString(datetime) {
    const date = DateTime.fromISO(datetime, { zone: 'utc' })
        .setZone('Asia/Ho_Chi_Minh', { keepLocalTime: true });

    return date.toFormat('yyyy-MM-dd HH:mm:ss');
}

module.exports = {
    getCurrentDateTime: getCurrentDateTime,
    toLocaleString: toLocaleString,
}
