const API = "http://localhost:8081";

/* ══════ SESSION ══════ */
function getToken(){ return localStorage.getItem('poolai_token'); }
function getUser(){
    try{ return JSON.parse(localStorage.getItem('poolai_user')||'null'); }
    catch{ return null; }
}
function logout(){
    localStorage.removeItem('poolai_token');
    localStorage.removeItem('poolai_user');
    window.location.href='login.html';
}
function requireAuth(){
    if(!getToken() && !window.location.pathname.endsWith('login.html')){
        window.location.href='login.html'; return false;
    }
    return true;
}

/* ══════ FETCH HELPERS ══════ */
// Use for write operations (need JWT)
async function apiFetch(url, options={}){
    const token = getToken();
    const headers = {'Content-Type':'application/json', ...(options.headers||{})};
    if(token) headers['Authorization'] = 'Bearer '+token;
    const res = await fetch(API+url, {...options, headers});
    if(res.status===401||res.status===403){
        toast('Session expired. Please login again.','err');
        setTimeout(logout, 1500);
        throw new Error('Unauthorized');
    }
    return res;
}

// Use for read operations (public endpoints, no token needed)
async function pubFetch(url){
    const res = await fetch(API+url);
    const json = await res.json();
    // Unwrap ApiResponse wrapper if present
    return json.data !== undefined ? json.data : json;
}

/* ══════ SIDEBAR ══════ */
function sidebar(active){
    const user = getUser();
    const ini2 = user ? user.name.split(' ').map(w=>w[0]).join('').slice(0,2).toUpperCase() : '--';
    const links=[
        {h:"index.html",    i:"fa-gauge-high",              l:"Dashboard",    g:null},
        {h:"find.html",     i:"fa-magnifying-glass-location",l:"Find a Ride",  g:"Passenger"},
        {h:"book.html",     i:"fa-ticket",                  l:"My Bookings",  g:null},
        {h:"offer.html",    i:"fa-car",                     l:"Offer a Ride", g:"Driver"},
        {h:"myrides.html",  i:"fa-list-check",              l:"My Rides",     g:null},
        {h:"schedules.html",i:"fa-calendar-days",           l:"Schedules",    g:null},
        {h:"users.html",    i:"fa-user-group",              l:"Users",        g:"Admin"},
        {h:"rides.html",    i:"fa-route",                   l:"All Rides",    g:null},
        {h:"vehicles.html", i:"fa-car-rear",                l:"Vehicles",     g:null},
        {h:"payments.html", i:"fa-wallet",                  l:"Payments",     g:null},
        {h:"ratings.html",  i:"fa-star-half-stroke",        l:"Ratings",      g:null},
        {h:"impact.html",   i:"fa-seedling",                l:"Eco Impact",   g:null},
        {h:"notifications.html",i:"fa-bell",               l:"Notifications", g:null},
        {h:"admin.html",    i:"fa-shield-halved",           l:"Admin",        g:null},
    ];
    let nav='';
    let lg=null;
    links.forEach(k=>{
        if(k.g&&k.g!==lg){ nav+=`<div class="nav-sec">${k.g}</div>`; lg=k.g; }
        nav+=`<a href="${k.h}" class="nav-item${k.h===active?' active':''}">
            <i class="fa-solid ${k.i} ni"></i><span>${k.l}</span>
            ${k.h===active?'<div class="n-dot"></div>':''}
        </a>`;
    });
    return `<aside class="sidebar">
        <div class="sb-logo">
            <div class="logo-mark">P</div>
            <div><div class="logo-text">PoolAI</div><div class="logo-sub">Smart Carpool</div></div>
        </div>
        <nav class="sb-nav">${nav}</nav>
        <div class="sb-user" onclick="logout()" title="Click to logout">
            <div class="ua">${ini2}</div>
            <div><div class="u-nm">${user?.name||'User'} <span style="font-size:0.6rem;color:var(--t3)"></span></div><div class="u-rl">${user?.role||'—'}</div></div>
            <i class="fa-solid fa-right-from-bracket" style="margin-left:auto;font-size:0.68rem;color:var(--t3)"></i>
        </div>
    </aside>`;
}

function topbar(title){
    return `<div class="topbar">
        <span class="tb-title">${title}</span>
        <div class="tb-search"><i class="fa-solid fa-magnifying-glass"></i><input placeholder="Search anything..."></div>
        <div class="tb-btn" onclick="window.location='notifications.html'" title="Notifications">
            <i class="fa-regular fa-bell"></i><div class="n-dot-abs"></div>
        </div>
        <div class="tb-btn" onclick="window.location='admin.html'" title="Admin">
            <i class="fa-solid fa-shield-halved"></i>
        </div>
    </div>`;
}

/* ══════ TOAST ══════ */
function _shelf(){
    if(!document.getElementById('_ts')){
        const s=document.createElement('div');s.id='_ts';s.className='ts-shelf';
        document.body.appendChild(s);
    }
    return document.getElementById('_ts');
}
function toast(msg, type='ok'){
    const icons={ok:'fa-circle-check',err:'fa-circle-exclamation',inf:'fa-circle-info',wrn:'fa-triangle-exclamation'};
    const t=document.createElement('div');
    t.className=`t-toast t-${type}`;
    t.innerHTML=`<i class="fa-solid ${icons[type]||icons.ok} ti"></i><span>${msg}</span>`;
    _shelf().appendChild(t);
    setTimeout(()=>{
        t.style.opacity='0'; t.style.transform='translateX(110%)'; t.style.transition='all 0.35s';
        setTimeout(()=>t.remove(), 380);
    }, 3800);
}

function openModal(id){ document.getElementById(id).classList.add('open'); }
function closeModal(id){ document.getElementById(id).classList.remove('open'); }

/* ══════ MAP — uses free OSM tiles (no API key needed) ══════ */
let _map=null;
function initMap(id='map'){
    const el=document.getElementById(id);
    if(!el||_map) return;
    _map=L.map(id,{zoomControl:true}).setView([20.59,78.96],5);
    // Use CartoDB Dark Matter - works without API key
    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_matter_all/{z}/{x}/{y}.png',{
        attribution:'© <a href="https://www.openstreetmap.org">OpenStreetMap</a> © <a href="https://carto.com">CARTO</a>',
        subdomains:'abcd', maxZoom:19
    }).addTo(_map);
}

async function geocode(q){
    if(!q) return null;
    try{
        const r = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(q+', India')}&limit=1`,
            {headers:{'User-Agent':'PoolAI/1.0'}});
        const d = await r.json();
        return d.length ? [parseFloat(d[0].lat), parseFloat(d[0].lon)] : null;
    }catch{ return null; }
}

async function plotRoute(src, dst){
    if(!_map) return null;
    _map.eachLayer(l=>{
        if(l instanceof L.Marker || l instanceof L.Polyline || l._leaflet_id && l.feature)
            _map.removeLayer(l);
    });
    const mkIcon = c => L.divIcon({
        className:'',
        html:`<div style="width:14px;height:14px;background:${c};border-radius:50%;border:2.5px solid #fff;box-shadow:0 0 10px ${c}88;"></div>`,
        iconAnchor:[7,7]
    });
    const [sc, dc] = await Promise.all([geocode(src), geocode(dst)]);
    if(sc) L.marker(sc,{icon:mkIcon('#818cf8')}).addTo(_map).bindPopup(`<b>📍 From:</b> ${src}`);
    if(dc) L.marker(dc,{icon:mkIcon('#34d399')}).addTo(_map).bindPopup(`<b>🏁 To:</b> ${dst}`);

    if(sc && dc){
        // Try OSRM real road routing
        try{
            const osrm = await fetch(
                `https://router.project-osrm.org/route/v1/driving/${sc[1]},${sc[0]};${dc[1]},${dc[0]}?overview=full&geometries=geojson`
            );
            const od = await osrm.json();
            if(od.routes && od.routes[0]){
                const gj = L.geoJSON(od.routes[0].geometry, {
                    style:{color:'#818cf8', weight:4, opacity:0.85}
                }).addTo(_map);
                _map.fitBounds(gj.getBounds(), {padding:[40,40]});
                const distKm = (od.routes[0].distance/1000).toFixed(1);
                const durMin = Math.round(od.routes[0].duration/60);
                return {dist:distKm, dur:durMin};
            }
        }catch{}
        // Fallback straight line
        const pl = L.polyline([sc,dc],{color:'#818cf8',weight:3,opacity:0.7,dashArray:'8,8'}).addTo(_map);
        _map.fitBounds(pl.getBounds(),{padding:[40,40]});
        return {dist:(L.latLng(sc).distanceTo(L.latLng(dc))/1000).toFixed(1), dur:null};
    }
    if(sc) _map.setView(sc,12);
    return null;
}

/* ══════ SEARCHABLE SELECT ══════ */
function makeSearchable(selId){
    const sel = document.getElementById(selId);
    if(!sel || sel._done) return;
    sel._done = true;
    const wrap = document.createElement('div');
    wrap.style.cssText = 'position:relative';
    sel.parentNode.insertBefore(wrap, sel);
    wrap.appendChild(sel);
    sel.style.display = 'none';

    const inp = document.createElement('input');
    inp.className = 'fi';
    inp.placeholder = 'Type to search...';
    inp.autocomplete = 'off';
    wrap.appendChild(inp);

    // Append to body to escape card's overflow:hidden clipping
    const drop = document.createElement('div');
    drop.style.cssText = 'position:fixed;background:var(--s2);border:1px solid var(--b1);border-radius:var(--r2);max-height:210px;overflow-y:auto;z-index:99999;display:none;box-shadow:0 10px 30px rgba(0,0,0,0.6)';
    document.body.appendChild(drop);

    function positionDrop(){
        const r = inp.getBoundingClientRect();
        drop.style.top   = (r.bottom + 2) + 'px';
        drop.style.left  = r.left + 'px';
        drop.style.width = r.width + 'px';
    }

    function renderDrop(filter=''){
        const opts = [...sel.options].filter(o=>o.value);
        const filtered = filter ? opts.filter(o=>o.text.toLowerCase().includes(filter.toLowerCase())) : opts;
        drop.innerHTML = filtered.length
            ? filtered.map(o=>`<div data-val="${o.value}" style="padding:8px 12px;cursor:pointer;font-size:0.82rem;border-bottom:1px solid var(--b0);transition:background 0.1s" 
                onmouseenter="this.style.background='var(--s3)'" 
                onmouseleave="this.style.background=''">${o.text}</div>`).join('')
            : `<div style="padding:10px 12px;font-size:0.8rem;color:var(--t3)">No results found</div>`;
        drop.querySelectorAll('[data-val]').forEach(d=>{
            d.onclick = ()=>{
                sel.value = d.dataset.val;
                inp.value = d.textContent.trim();
                drop.style.display = 'none';
                sel.dispatchEvent(new Event('change'));
            };
        });
    }

    inp.onfocus = ()=>{ positionDrop(); renderDrop(inp.value); drop.style.display='block'; };
    inp.oninput = ()=>{ positionDrop(); renderDrop(inp.value); };
    inp.onblur  = ()=>setTimeout(()=>drop.style.display='none', 200);
    window.addEventListener('scroll', ()=>{ if(drop.style.display!=='none') positionDrop(); }, true);
    window.addEventListener('resize', ()=>{ if(drop.style.display!=='none') positionDrop(); });

    // Preselect if sel already has a value
    function syncDisplay(){
        const cur = sel.options[sel.selectedIndex];
        if(cur && cur.value) inp.value = cur.text;
    }
    new MutationObserver(syncDisplay).observe(sel,{childList:true,attributes:true});
    syncDisplay();
}

/* ══════ SMART DROPDOWNS — public endpoints, no token needed ══════ */
async function populateUserSelect(selId, placeholder='Select user...', roleFilter=''){
    const sel = document.getElementById(selId);
    if(!sel) return;
    sel.innerHTML = `<option value="">Loading users...</option>`;
    try{
        const users = await pubFetch('/pool/all');
        const me = getUser();
        const filtered = roleFilter ? users.filter(u=>u.role===roleFilter||u.role==='BOTH') : users;
        sel.innerHTML = `<option value="">${placeholder}</option>` +
            filtered.map(u=>`<option value="${u.id}" ${me&&u.id==me.id?'selected':''}>
                #${u.id} · ${u.name} (${u.role||'User'})${u.homeLocation?' — '+u.homeLocation:''}
            </option>`).join('');
        setTimeout(()=>makeSearchable(selId), 50);
    }catch(e){
        sel.innerHTML = `<option value="">Error loading users — refresh page</option>`;
        console.error('populateUserSelect error:', e);
    }
}

async function populateRideSelect(selId, placeholder='Select ride...', onlyActive=false){
    const sel = document.getElementById(selId);
    if(!sel) return;
    sel.innerHTML = `<option value="">Loading rides...</option>`;
    try{
        const url = onlyActive ? '/ride/active' : '/ride/all';
        const rides = await pubFetch(url);
        sel.innerHTML = `<option value="">${placeholder}</option>` +
            rides.map(r=>`<option value="${r.id}">#${r.id} · ${r.sourceLocation||'Unknown'}→${r.destinationLocation||'Unknown'} (${r.rideDate||'TBD'} ${fT(r.rideTime)}) [${r.availableSeats||0} seats]${r.pricePerSeat?' ₹'+r.pricePerSeat+'/seat':''}</option>`).join('');
        setTimeout(()=>makeSearchable(selId), 50);
    }catch(e){
        sel.innerHTML = `<option value="">Error loading rides</option>`;
    }
}

async function populateParticipationSelect(selId){
    const sel = document.getElementById(selId);
    if(!sel) return;
    sel.innerHTML = `<option value="">Loading bookings...</option>`;
    try{
        const parts = await pubFetch('/participation/all');
        sel.innerHTML = `<option value="">Select booking...</option>` +
            parts.filter(p=>p.status!=='CANCELLED').map(p=>`<option value="${p.id}">#${p.id} · ${p.user?.name||'User'} on Ride #${p.ride?.id||''} (${p.ride?.sourceLocation||'Unknown'}→${p.ride?.destinationLocation||'Unknown'}) [${p.seatsBooked} seat(s)]</option>`).join('');
        setTimeout(()=>makeSearchable(selId), 50);
    }catch(e){
        sel.innerHTML = `<option value="">Error loading bookings</option>`;
    }
}

/* ══════ HELPERS ══════ */
function sbadge(s){
    const m={PENDING:'b-p',ACCEPTED:'b-a',ONGOING:'b-o',COMPLETED:'b-c',CANCELLED:'b-x',CONFIRMED:'b-cf'};
    return `<span class="badge ${m[s]||'b-i'}">${s||'—'}</span>`;
}
function rbadge(r){
    return r?`<span class="badge ${r==='DRIVER'?'b-a':r==='RIDER'?'b-o':'b-c'}">${r}</span>`:'';
}
function stars(n, sz='0.66rem'){
    return `<span class="sr" style="font-size:${sz}">${
        Array.from({length:5},(_,i)=>`<i class="fa-${i<Math.round(n)?'solid':'regular'} fa-star${i>=Math.round(n)?' e':''}"></i>`).join('')
    }</span>`;
}
function aiLabel(s){ return s>=85?['ai-ex','Excellent']:s>=65?['ai-gt','Great']:s>=45?['ai-gd','Good']:['ai-lo','Low Match']; }
function aiColor(s){ return s>=85?'var(--em)':s>=65?'var(--ind)':s>=45?'var(--amb)':'var(--rose)'; }
function fT(t){ return t?t.slice(0,5):'—'; }
function fM(n){ return '₹'+Number(n||0).toLocaleString('en-IN'); }
function ini(name){ return name?name.split(' ').map(w=>w[0]).join('').slice(0,2).toUpperCase():'--'; }
function gradFor(i){
    const g=[['#818cf8','#6366f1'],['#34d399','#10b981'],['#f472b6','#ec4899'],
             ['#fb923c','#f59e0b'],['#38bdf8','#0ea5e9'],['#a78bfa','#7c3aed']];
    const p=g[Math.abs(i||0)%g.length];
    return `linear-gradient(135deg,${p[0]},${p[1]})`;
}

/* ══════ RIDE CARD ══════ */
function rideCard(r, showScore=false, showBook=false){
    const [cls,lbl] = aiLabel(r.aiMatchScore||0);
    const scoreBar = showScore ? `<div class="ai-wrap">
        <div class="ai-top"><span class="fs0 cm">AI Match</span>
        <span class="ai-pill ${cls}">${lbl} · ${r.aiMatchScore||0}%</span></div>
        <div class="ai-bar"><div class="ai-fill" style="width:${r.aiMatchScore||0}%;background:${aiColor(r.aiMatchScore||0)}"></div></div>
    </div>` : '';
    const driver = r.user ? `<div class="rc-drv">
        <div class="uca" style="width:26px;height:26px;font-size:0.6rem;background:${gradFor(r.user.id||0)}">${ini(r.user.name)}</div>
        <div style="flex:1;min-width:0">
            <div class="fs1 fw6">${r.user.name||'—'}</div>
            <div class="fs0 cm">${r.user.phone||r.user.email||''}</div>
        </div>
        ${stars(r.user.rating||5)}
    </div>` : '';
    const price = r.pricePerSeat!=null ? `<div style="text-align:right;flex-shrink:0">
        <div class="price-big">${fM(r.pricePerSeat)}</div>
        <div class="price-lbl">per seat</div>
    </div>` : '';

    return `<div class="rc ${showScore&&(r.aiMatchScore||0)>=75?'top':''}">
        <div class="fb" style="gap:10px;margin-bottom:10px">
            <div class="rc-route" style="flex:1">
                <div class="rc-loc"><div class="dot-a"></div><span class="fw6">${r.sourceLocation||'—'}</span></div>
                <div class="rc-line"></div>
                <div class="rc-loc"><div class="dot-b"></div><span class="fw6">${r.destinationLocation||'—'}</span></div>
            </div>${price}
        </div>
        <div class="rc-meta">
            <span class="mt"><i class="fa-regular fa-calendar"></i>${r.rideDate||'—'}</span>
            <span class="mt"><i class="fa-regular fa-clock"></i>${fT(r.rideTime)}</span>
            <span class="mt"><i class="fa-solid fa-user-check"></i>${r.availableSeats??0} seats</span>
            ${r.vehicleType?`<span class="mt"><i class="fa-solid fa-car"></i>${r.vehicleType}</span>`:''}
            ${r.acAvailable?`<span class="chip" style="font-size:0.62rem;color:var(--sky)"><i class="fa-solid fa-wind"></i>AC</span>`:''}
            ${r.ladiesOnly?`<span class="chip" style="font-size:0.62rem;color:var(--rose)"><i class="fa-solid fa-venus"></i>Ladies</span>`:''}
            <span style="margin-left:auto">${sbadge(r.status)}</span>
        </div>
        ${driver}${scoreBar}
        ${showBook?`<div class="rc-acts">
            <button class="btn btn-em btn-sm" onclick="event.stopPropagation();doBook(${r.id})">
                <i class="fa-solid fa-handshake-simple"></i> Book Now
            </button>
            <button class="btn btn-gh btn-sm" onclick="event.stopPropagation();plotRoute('${r.sourceLocation||''}','${r.destinationLocation||''}')">
                <i class="fa-solid fa-map"></i> Map
            </button>
        </div>`:scoreBar?`<div class="rc-acts">
            <button class="btn btn-gh btn-sm" onclick="event.stopPropagation();plotRoute('${r.sourceLocation||''}','${r.destinationLocation||''}')">
                <i class="fa-solid fa-map"></i> View Route
            </button>
        </div>`:''}
    </div>`;
}

/* ══════ BOOK FLOW ══════ */
function doBook(rideId){
    const me = getUser();
    const mod = document.createElement('div');
    mod.className = 'ov open';
    mod.innerHTML = `<div class="modal">
        <div class="mh">
            <h3><i class="fa-solid fa-handshake-simple" style="color:var(--em)"></i> Book Ride #${rideId}</h3>
            <button class="mcl" onclick="this.closest('.ov').remove()"><i class="fa-solid fa-xmark"></i></button>
        </div>
        ${me?`<div style="background:rgba(52,211,153,0.08);border:1px solid rgba(52,211,153,0.2);border-radius:var(--r2);padding:10px 13px;margin-bottom:12px" class="fc g2u">
            <i class="fa-solid fa-circle-check" style="color:var(--em)"></i>
            <span class="fs1">Logged in as <strong>${me.name}</strong> (User #${me.id})</span>
        </div>`:''}
        <div class="fgr mb3">
            <label class="flb">Select Passenger</label>
            <select id="_buid" class="fsel"><option value="">Loading...</option></select>
        </div>
        <div class="fgr mb3">
            <label class="flb">Seats Needed</label>
            <select id="_bseats" class="fsel">
                <option value="1">1 Seat</option><option value="2">2 Seats</option>
                <option value="3">3 Seats</option><option value="4">4 Seats</option>
            </select>
        </div>
        <div class="mf">
            <button class="btn btn-gh" onclick="this.closest('.ov').remove()">Cancel</button>
            <button class="btn btn-em" onclick="confirmBook(${rideId})">
                <i class="fa-solid fa-check"></i> Confirm Booking
            </button>
        </div>
    </div>`;
    document.body.appendChild(mod);
    populateUserSelect('_buid','— Select passenger —','');
    // Auto-select logged-in user after dropdown loads
    if(me){
        setTimeout(()=>{
            const s = document.getElementById('_buid');
            if(s){ s.value = me.id; s.dispatchEvent(new Event('change')); }
        }, 1200);
    }
}

function confirmBook(rideId){
    const uid   = document.getElementById('_buid').value;
    const seats = document.getElementById('_bseats').value;
    if(!uid){ toast('Please select a passenger','err'); return; }
    apiFetch(`/participation/join?rideId=${rideId}&userId=${uid}&seats=${seats}`,{method:'POST'})
        .then(r=>{ if(!r.ok) return r.json().then(e=>{throw new Error(e.message||'Failed');}); return r.json(); })
        .then(j=>{
            const p = j.data||j;
            document.querySelector('.ov.open')?.remove();
            toast(`✅ Booked! Booking ID: #${p.id} — save this for payments & ratings`,'ok');
        })
        .catch(e=>toast(e.message,'err'));
}

/* ══════ SPLASH ══════ */
function splash(){
    if(!requireAuth()) return;
    const u = getUser();
    const s = document.createElement('div');
    s.style.cssText = 'position:fixed;inset:0;background:var(--bg);z-index:10000;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:12px;transition:opacity 0.5s';
    s.innerHTML = `
        <div style="width:48px;height:48px;border-radius:13px;background:linear-gradient(135deg,#6366f1,#4f46e5);display:flex;align-items:center;justify-content:center;font-size:1.3rem;font-weight:800;color:#fff;box-shadow:0 0 0 1px rgba(99,102,241,0.5),0 4px 20px rgba(79,70,229,0.5)">P</div>
        <div style="font-weight:700;font-size:1.15rem;color:#f4f4f5">PoolAI</div>
        ${u?`<div style="font-size:0.78rem;color:#52525b">Welcome back, ${u.name}</div>`:''}
        <div style="width:120px;height:2px;background:#27272c;border-radius:1px;overflow:hidden;position:relative">
            <div style="position:absolute;height:100%;width:45%;background:linear-gradient(90deg,#6366f1,#34d399);border-radius:1px;animation:_lb 0.9s ease-in-out infinite alternate"></div>
        </div>
        <style>@keyframes _lb{from{left:-45%}to{left:100%}}</style>`;
    document.body.appendChild(s);
    setTimeout(()=>{ s.style.opacity='0'; setTimeout(()=>s.remove(),480); }, 950);
}
document.addEventListener('DOMContentLoaded', splash);
