import {create} from 'zustand';
import HeadlineData from "./headlines/headline-data.ts";

interface AppState {
    hours: number;
    setHours: (hours: number) => void;
    headlines: HeadlineData[];
    addHeadlines: (headlines: HeadlineData[]) => void;
    setHeadlines: (headlines: HeadlineData[]) => void;
    selectedKeywords: string[];
    toggleKeyword: (keyword: string) => void;
    similiarHeadlinesList: { id: string };
    setSimiliarHeadlinesList: (similiarHeadlinesList: { id: string }) => void;
}

export const useStore = create<AppState>((set) => ({
    hours: 3,
    setHours: (hours) => set({hours}),
    headlines: [],
    addHeadlines: (headlines) =>
        set((state) => ({headlines: [...state.headlines, ...headlines]})),
    setHeadlines: (headlines) => set({headlines}),
    selectedKeywords: [],
    toggleKeyword: (keyword: string) =>
        set((state) => ({
            selectedKeywords: state.selectedKeywords.includes(keyword)
                ? state.selectedKeywords.filter((k) => k !== keyword)
                : [...state.selectedKeywords, keyword],
        })),
    similiarHeadlinesList: {id: ""},
    setSimiliarHeadlinesList: (similiarHeadlinesList) => set({similiarHeadlinesList}),
}));