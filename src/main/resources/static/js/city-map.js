document.addEventListener('DOMContentLoaded', function () {
  const container = document.getElementById('city-operations-map');
  if (!container) return;

  const info = document.getElementById('map-filter-note');
  const nodes = Array.from(document.querySelectorAll('[data-map-site]'));
  const sites = nodes.map((node) => ({
    name: node.dataset.name,
    lat: Number(node.dataset.lat),
    lng: Number(node.dataset.lng),
    powered: node.dataset.powered === 'true',
    usage: Number(node.dataset.usage)
  })).filter((site) => Number.isFinite(site.lat) && Number.isFinite(site.lng));

  if (typeof L === 'undefined') {
    container.classList.add('map-unavailable');
    container.textContent = 'Interactive map requires an internet connection. Location and status details are shown alongside.';
    return;
  }
  const map = L.map(container, { scrollWheelZoom: false, zoomControl: false, preferCanvas: true })
    .setView([6.9250, 79.8620], 13);
  L.control.zoom({ position: 'bottomright' }).addTo(map);
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright" target="_blank" rel="noopener">OpenStreetMap contributors</a>'
  }).addTo(map);
  const markers = sites.map((site) => {
    const icon = L.divIcon({
      className: 'asset-marker-wrapper',
      html: '<span class="asset-marker ' + (site.powered ? 'asset-marker-on' : 'asset-marker-off') + '"><span class="asset-marker-core"></span></span>',
      iconSize: [30, 30],
      iconAnchor: [15, 15]
    });
    const marker = L.marker([site.lat, site.lng], {icon});
    const popup = document.createElement('div');
    popup.className = 'asset-popup';
    const title = document.createElement('strong');
    title.textContent = site.name;
    const status = document.createElement('p');
    status.textContent = (site.powered ? 'Powered on' : 'Power off') + ' · ' + site.usage + ' kW';
    const link = document.createElement('a');
    link.href = '/zones';
    link.textContent = 'Manage city zones →';
    popup.append(title, status, link);
    marker.bindPopup(popup);
    marker.addTo(map);
    return {site, marker};
  });

  const initialBounds = L.latLngBounds(sites.map((site) => [site.lat, site.lng]));
  const fit = () => {
    if (initialBounds.isValid()) map.fitBounds(initialBounds.pad(.32), {maxZoom: 14});
  };
  fit();
  function applyFilter(filter) {
    let count = 0;
    markers.forEach(({site, marker}) => {
      const visible = filter === 'all' || (filter === 'on' ? site.powered : !site.powered);
      if (visible) {
        if (!map.hasLayer(marker)) marker.addTo(map);
        count++;
      } else if (map.hasLayer(marker)) {
        map.removeLayer(marker);
      }
    });
    if (info) info.textContent = count + (count === 1 ? ' demo asset shown' : ' demo assets shown');
  }
  document.querySelectorAll('[data-map-filter]').forEach((button) => {
    button.addEventListener('click', () => {
      document.querySelectorAll('[data-map-filter]').forEach((other) => {
        const active = other === button;
        other.classList.toggle('is-active', active);
        other.setAttribute('aria-pressed', String(active));
      });
      applyFilter(button.dataset.mapFilter);
    });
  });
  document.querySelector('[data-map-reset]')?.addEventListener('click', fit);
  map.whenReady(() => setTimeout(() => map.invalidateSize(), 100));
});
