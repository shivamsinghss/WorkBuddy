/* ===== WorkBuddy API Helper ===== */
/* API_BASE is declared in auth.js — must be loaded first */

async function apiFetch(path, options = {}) {
    const res = await fetch(API_BASE + path, {
        headers: { 'Content-Type': 'application/json' },
        ...options
    });
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const text = await res.text();
    return text ? JSON.parse(text) : null;
}

const Api = {
    employees: {
        getAll:  ()       => apiFetch('/api/employees'),
        getById: (id)     => apiFetch('/api/employees/' + id),
        create:  (data)   => apiFetch('/api/employees',      { method: 'POST',   body: JSON.stringify(data) }),
        update:  (id, d)  => apiFetch('/api/employees/' + id, { method: 'PUT',   body: JSON.stringify(d) }),
        delete:  (id)     => apiFetch('/api/employees/' + id, { method: 'DELETE' })
    },
    leaves: {
        getAll:  ()       => apiFetch('/api/leaves'),
        submit:  (data)   => apiFetch('/api/leaves',            { method: 'POST',  body: JSON.stringify(data) }),
        approve: (id)     => apiFetch('/api/leaves/' + id + '/approve', { method: 'PUT' }),
        reject:  (id)     => apiFetch('/api/leaves/' + id + '/reject',  { method: 'PUT' }),
        update:  (id, d)  => apiFetch('/api/leaves/' + id,     { method: 'PUT',   body: JSON.stringify(d) }),
        delete:  (id)     => apiFetch('/api/leaves/' + id,     { method: 'DELETE' })
    },
    payroll: {
        getAll:   ()      => apiFetch('/api/payroll'),
        create:   (data)  => apiFetch('/api/payroll',           { method: 'POST',  body: JSON.stringify(data) }),
        markPaid: (id)    => apiFetch('/api/payroll/' + id + '/pay', { method: 'PUT' }),
        update:   (id, d) => apiFetch('/api/payroll/' + id,    { method: 'PUT',   body: JSON.stringify(d) }),
        delete:   (id)    => apiFetch('/api/payroll/' + id,    { method: 'DELETE' })
    },
    performance: {
        getAll:  ()       => apiFetch('/api/performance'),
        create:  (data)   => apiFetch('/api/performance',       { method: 'POST',  body: JSON.stringify(data) }),
        update:  (id, d)  => apiFetch('/api/performance/' + id, { method: 'PUT',  body: JSON.stringify(d) }),
        delete:  (id)     => apiFetch('/api/performance/' + id, { method: 'DELETE' })
    },
    training: {
        getAll:  ()       => apiFetch('/api/training'),
        create:  (data)   => apiFetch('/api/training',          { method: 'POST',  body: JSON.stringify(data) }),
        update:  (id, d)  => apiFetch('/api/training/' + id,   { method: 'PUT',   body: JSON.stringify(d) }),
        delete:  (id)     => apiFetch('/api/training/' + id,   { method: 'DELETE' })
    },
    attendance: {
        getToday:    (empId)        => apiFetch('/api/attendance/today/' + empId),
        getHistory:  (empId)        => apiFetch('/api/attendance/history/' + empId),
        getAllToday:  ()             => apiFetch('/api/attendance/all'),
        getBalance:  (empId, name)  => apiFetch('/api/attendance/balance/' + empId + '?name=' + encodeURIComponent(name||'')),
        mark:        (data)         => apiFetch('/api/attendance/mark',      { method: 'POST', body: JSON.stringify(data) }),
        punchIn:     (data)         => apiFetch('/api/attendance/punch-in',  { method: 'POST', body: JSON.stringify(data) }),
        punchOut:    (data)         => apiFetch('/api/attendance/punch-out', { method: 'POST', body: JSON.stringify(data) }),
        addCredits:  (data)         => apiFetch('/api/attendance/add-credits', { method: 'POST', body: JSON.stringify(data) })
    },
    recruitment: {
        getAll:  ()       => apiFetch('/api/recruitment'),
        create:  (data)   => apiFetch('/api/recruitment',       { method: 'POST',  body: JSON.stringify(data) }),
        close:   (id)     => apiFetch('/api/recruitment/' + id + '/close', { method: 'PUT' }),
        update:  (id, d)  => apiFetch('/api/recruitment/' + id, { method: 'PUT',  body: JSON.stringify(d) }),
        delete:  (id)     => apiFetch('/api/recruitment/' + id, { method: 'DELETE' })
    }
};
