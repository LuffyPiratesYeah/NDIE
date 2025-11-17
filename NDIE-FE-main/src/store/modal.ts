import { create } from 'zustand';

type Store = {
  isModalOpen: boolean;
  toggleModal: () => void;
};

export const useModalStore = create<Store>((set) => ({
  isModalOpen: false,
  toggleModal: () => set((s) => ({ isModalOpen: !s.isModalOpen })),
}));