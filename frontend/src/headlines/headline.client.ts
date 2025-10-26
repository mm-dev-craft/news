import axios from 'axios';
import HeadlineData from './headline-data.ts';

const apiClient = axios.create({
    baseURL: '/api/news', // Use the proxied base URL
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getAllHeadlines = async (pastHours?: number): Promise<HeadlineData[]> => {
    const response = await apiClient.get<HeadlineData[]>('/all', {
        params: { pastHours },
    });
    return response.data;
};