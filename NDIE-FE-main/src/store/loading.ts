import { create } from 'zustand';

type Store = {
  isLoading: boolean;
  setIsLoadingTrue: () => void;
  setIsLoadingFalse : () => void;
};

export const useLoadingStore = create<Store>((set) => ({
  isLoading: false,
  setIsLoadingTrue: () => set(() => ({ isLoading: true })),
  setIsLoadingFalse: () => set(() => ({ isLoading: false })),
}));