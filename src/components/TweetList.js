import React from 'react'

import { View } from 'react-native'

import Tweet from './Tweet.js'

export default class TweetList extends React.Component {
  render() {
    return (
      <View style={styles.list}>
        {this.props.tweets.map((tweet) => {
          return <Tweet text={tweet.text} key={tweet.id} />
        })}
      </View>
    )
  }
}

const styles = {
  list: {
    flex: 1,
    flexDirection: 'column',
  },
}
