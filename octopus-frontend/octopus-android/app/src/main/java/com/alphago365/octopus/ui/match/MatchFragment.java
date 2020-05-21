package com.alphago365.octopus.ui.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphago365.octopus.R;
import com.alphago365.octopus.adapter.MatchRecyclerViewAdapter;
import com.alphago365.octopus.mvp.model.ListItem;
import com.alphago365.octopus.mvp.model.Match;
import com.alphago365.octopus.mvp.presenter.MatchPresenter;
import com.alphago365.octopus.mvp.presenter.MatchPresenterImpl;
import com.alphago365.octopus.mvp.repository.match.MatchLocalRepository;
import com.alphago365.octopus.mvp.repository.match.MatchLocalRepositoryImpl;
import com.alphago365.octopus.mvp.repository.match.MatchRemoteRepository;
import com.alphago365.octopus.mvp.repository.match.MatchRemoteRepositoryImpl;
import com.alphago365.octopus.mvp.repository.match.MatchRepository;
import com.alphago365.octopus.mvp.repository.match.MatchRepositoryImpl;
import com.alphago365.octopus.mvp.view.BaseFragment;
import com.alphago365.octopus.mvp.view.MatchView;
import com.alphago365.octopus.payload.MatchChild;
import com.alphago365.octopus.payload.MatchHeader;
import com.alphago365.octopus.persistence.AppDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MatchFragment extends BaseFragment<MatchPresenter> implements MatchView {

    private MatchViewModel matchViewModel;

    private RecyclerView mRecyclerView;
    private MatchRecyclerViewAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_match, container, false);
        mRecyclerView = root.findViewById(R.id.rv_matches);
        initRecyclerView();
        matchViewModel.getLiveData().observe(getViewLifecycleOwner(), matchList -> {
            List<ListItem> groupedItemList = createGroupedItemList(matchList);
            mAdapter.submitList(groupedItemList);
        });
        return root;
    }

    @NotNull
    private List<ListItem> createGroupedItemList(List<Match> matchList) {
        List<ListItem> groupedItemList = new ArrayList<>();
        if (matchList == null || matchList.isEmpty()) {
            return groupedItemList;
        }
        matchList.stream().collect(Collectors.groupingBy(Match::getDate))
                .forEach((K, V) -> {
                    MatchHeader header = new MatchHeader();
                    header.setDate(K);
                    header.setDescription(String.format(Locale.getDefault(), getString(R.string.n_matches), V.size()));
                    groupedItemList.add(header);
                    V.sort(Comparator.comparing(Match::getKickoffTime));
                    V.forEach(match -> {
                        MatchChild child = new MatchChild(match);
                        groupedItemList.add(child);
                    });
                });
        return groupedItemList;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().showMatchList("2020-05-18", 1);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new MatchRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showMatchList(List<Match> matchList) {
        matchViewModel.setLiveData(matchList);
    }

    @Override
    protected MatchPresenter createPresenter() {
        MatchRemoteRepository matchRemoteRepository = new MatchRemoteRepositoryImpl();
        AppDatabase instance = AppDatabase.getInstance(requireContext().getApplicationContext());
        MatchLocalRepository matchLocalRepository = new MatchLocalRepositoryImpl(instance.matchDao());
        MatchRepository matchRepository = new MatchRepositoryImpl(matchRemoteRepository, matchLocalRepository);
        return new MatchPresenterImpl(matchRepository, AndroidSchedulers.mainThread());
    }
}
