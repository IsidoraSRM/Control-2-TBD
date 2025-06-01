import L from 'leaflet';

export const getMarkerIcon = (status) => {
  const iconColor = status === 'completed' ? '#3b82f6' : '#60a5fa';
  const shadowColor = status === 'completed' ? 'rgba(59, 130, 246, 0.5)' : 'rgba(96, 165, 250, 0.5)';
  
  return L.divIcon({
    html: `
      <div class="custom-marker ${status}" style="
        width: 24px;
        height: 24px;
        background: ${iconColor};
        border-radius: 50%;
        box-shadow: 0 0 15px ${shadowColor};
        transition: transform 0.3s ease;
      "></div>
    `,
    className: 'custom-marker-container',
    iconSize: [24, 24],
    iconAnchor: [12, 12]
  });
};

// Estilos CSS que necesitas agregar a tu aplicación
export const markerStyles = `
  .custom-marker-container {
    background: transparent;
    border: none;
  }
  
  .custom-marker:hover {
    transform: scale(1.2);
  }
`; 