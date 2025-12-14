// utils/imageUtils.js
import { API_BASE_URL, API_ENDPOINTS } from '../config/api';

export const getImageUrl = (imageUrl) => {
  if (!imageUrl) return '';
  if (imageUrl.startsWith('http')) return imageUrl;
  return `${API_ENDPOINTS.UPLOADS.IMAGES}/${imageUrl}`;
};